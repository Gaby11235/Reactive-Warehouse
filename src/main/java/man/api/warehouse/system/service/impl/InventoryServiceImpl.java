package man.api.warehouse.system.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.AllArgsConstructor;
import man.api.warehouse.system.mapper.InventoryMapper;
import man.api.warehouse.system.model.Inventory;
import man.api.warehouse.system.model.dto.InventoryDto;
import man.api.warehouse.system.repository.InventoryReactiveRepository;
import man.api.warehouse.system.service.InventoryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private InventoryReactiveRepository inventoryReactiveRepository;
    private InventoryMapper inventoryMapper;

    public static final String INV_RES = "invResource";

    private static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource(INV_RES);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 10.
        rule.setCount(10);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    @Override
    public Mono<InventoryDto> save(InventoryDto inventoryDto) {
        initFlowRules();
        Entry entry = null;
        try {
            // 流控代码
            entry = SphU.entry(INV_RES);
            // 业务代码
            Inventory inventory = inventoryMapper.toModel(inventoryDto);
            inventory.setCreatedDate(LocalDateTime.now());
            inventory.setLastModifiedDate(LocalDateTime.now());
            return inventoryReactiveRepository.save(inventory).map(i -> {
                        inventoryDto.setId(i.getId());
                        return inventoryDto;
                    }
            );
        }catch(BlockException e){
            // 被限流了
            System.out.println("[saveInventory] has been protected! Time=" + System.currentTimeMillis());
        }finally {
            if(entry != null){
                entry.exit();
            }
        }
        return null;
    }

    @Override
    public Flux<InventoryDto> findAllInventories() {
        return inventoryReactiveRepository.findAll()
                .map(inventoryMapper::toDto)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<InventoryDto> update(InventoryDto inventoryDto, String id) {
        return inventoryReactiveRepository.findById(id)
                .flatMap(existingInventory -> {
                    Inventory inventory = inventoryMapper.toModel(inventoryDto);
                    inventory.setId(existingInventory.getId());
                    return inventoryReactiveRepository.save(inventory);
                })
                .map(inventoryMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return inventoryReactiveRepository.deleteById(id);
    }
}
