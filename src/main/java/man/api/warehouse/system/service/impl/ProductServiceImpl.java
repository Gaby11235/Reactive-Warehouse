package man.api.warehouse.system.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.AllArgsConstructor;
import man.api.warehouse.system.mapper.ProductMapper;
import man.api.warehouse.system.model.Product;
import man.api.warehouse.system.model.dto.ProductDto;
import man.api.warehouse.system.repository.ProductReactiveRepository;
import man.api.warehouse.system.service.ProductService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductReactiveRepository productReactiveRepository;
    private ProductMapper productMapper;

    public static final String STU_RES = "stuResource";

    private static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource(STU_RES);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 10.
        rule.setCount(10);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    @Override
    public Mono<ProductDto> save(ProductDto productDto) {
        initFlowRules();
        Entry entry = null;
        try {
            // 流控代码
            entry = SphU.entry(STU_RES);
            // 业务代码
            Product product = productMapper.toModel(productDto);
            product.setCreatedDate(LocalDateTime.now());
            product.setLastModifiedDate(LocalDateTime.now());
            return productReactiveRepository.save(product).map(p -> {
                        productDto.setId(p.getId());
                        return productDto;
                    }
            );
        }catch(BlockException e){
            // 被限流了
            System.out.println("[getUser] has been protected! Time="+System.currentTimeMillis());
        }finally {
            if(entry!=null){
                entry.exit();
            }
        }
        return null;
    }

    @Override
    public Flux<ProductDto> findAllProducts() {
        return productReactiveRepository.findAll()
                .map(productMapper::toDto)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<ProductDto> update(ProductDto productDto, String id) {
        return productReactiveRepository.findById(id)
                .flatMap(existingProduct -> {
                    Product product = productMapper.toModel(productDto);
                    product.setId(existingProduct.getId());
                    return productReactiveRepository.save(product);
                })
                .map(productMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return productReactiveRepository.deleteById(id);
    }
}
