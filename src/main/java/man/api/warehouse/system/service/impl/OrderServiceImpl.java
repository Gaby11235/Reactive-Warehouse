package man.api.warehouse.system.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.AllArgsConstructor;
import man.api.warehouse.system.mapper.OrderMapper;
import man.api.warehouse.system.model.Order;
import man.api.warehouse.system.model.dto.OrderDto;
import man.api.warehouse.system.repository.OrderReactiveRepository;
import man.api.warehouse.system.service.OrderService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderReactiveRepository orderReactiveRepository;
    private OrderMapper orderMapper;

    public static final String ORD_RES = "orderResource";

    private static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource(ORD_RES);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 10.
        rule.setCount(10);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    @Override
    public Mono<OrderDto> save(OrderDto orderDto) {
        initFlowRules();
        // Sentinel流控代码
        Entry entry = null;
        try {
            entry = SphU.entry(ORD_RES);
            // 业务逻辑
            Order order = orderMapper.toModel(orderDto);
            order.setCreatedDate(LocalDateTime.now());
            order.setLastModifiedDate(LocalDateTime.now());
            return orderReactiveRepository.save(order)
                    .map(orderMapper::toDto);
        } catch (BlockException e) {
            // 被限流处理
            System.out.println("[saveOrder] has been protected! Time=" + System.currentTimeMillis());
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
        return null;
    }

    @Override
    public Flux<OrderDto> findAllOrders() {
        return orderReactiveRepository.findAll()
                .map(orderMapper::toDto);
    }

    @Override
    public Mono<OrderDto> findOrderById(String id) {
        return null;
    }

    @Override
    public Mono<OrderDto> update(OrderDto orderDto, String id) {
        return orderReactiveRepository.findById(id)
                .flatMap(existingOrder -> {
                    Order order = orderMapper.toModel(orderDto);
                    order.setId(existingOrder.getId());
                    order.setCreatedDate(existingOrder.getCreatedDate()); // 不更新创建日期
                    order.setLastModifiedDate(LocalDateTime.now());
                    return orderReactiveRepository.save(order);
                })
                .map(orderMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return orderReactiveRepository.deleteById(id);
    }
}
