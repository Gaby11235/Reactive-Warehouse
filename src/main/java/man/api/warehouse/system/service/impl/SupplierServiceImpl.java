package man.api.warehouse.system.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.AllArgsConstructor;
import man.api.warehouse.system.mapper.SupplierMapper;
import man.api.warehouse.system.model.Supplier;
import man.api.warehouse.system.model.dto.SupplierDto;
import man.api.warehouse.system.repository.SupplierReactiveRepository;
import man.api.warehouse.system.service.SupplierService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private SupplierReactiveRepository supplierReactiveRepository;
    private SupplierMapper supplierMapper;

    public static final String SUP_RES = "supResource";

    private static void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource(SUP_RES);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 10.
        rule.setCount(10);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    @Override
    public Mono<SupplierDto> save(SupplierDto supplierDto) {
        initFlowRules();
        Entry entry = null;
        try {
            // 流控代码
            entry = SphU.entry(SUP_RES);
            // 业务代码
            Supplier supplier = supplierMapper.toModel(supplierDto);
            supplier.setCreatedDate(LocalDateTime.now());
            supplier.setLastModifiedDate(LocalDateTime.now());
            return supplierReactiveRepository.save(supplier).map(s -> {
                        supplierDto.setId(s.getId());
                        return supplierDto;
                    }
            );
        } catch (BlockException e) {
            // 被限流了
            System.out.println("[getSupplier] has been protected! Time=" + System.currentTimeMillis());
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
        return null;
    }

    @Override
    public Flux<SupplierDto> findAllSuppliers() {
        return supplierReactiveRepository.findAll()
                .map(supplierMapper::toDto)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<SupplierDto> update(SupplierDto supplierDto, String id) {
        return supplierReactiveRepository.findById(id)
                .flatMap(existingSupplier -> {
                    Supplier supplier = supplierMapper.toModel(supplierDto);
                    supplier.setId(existingSupplier.getId());
                    return supplierReactiveRepository.save(supplier);
                })
                .map(supplierMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return supplierReactiveRepository.deleteById(id);
    }
}
