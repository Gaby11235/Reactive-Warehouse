package man.api.warehouse.system.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.AllArgsConstructor;
import man.api.warehouse.system.mapper.SPRelationMapper;
import man.api.warehouse.system.model.SPRelation;
import man.api.warehouse.system.model.dto.SPRelationDto;
import man.api.warehouse.system.repository.SPRelationReactiveRepository;
import man.api.warehouse.system.service.SPRelationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SPRelationServiceImpl implements SPRelationService {

    private SPRelationReactiveRepository spRelationReactiveRepository;
    private SPRelationMapper spRelationMapper;

    public static final String REL_RES = "relResource";

    private static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource(REL_RES);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 10.
        rule.setCount(10);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    @Override
    public Mono<SPRelationDto> save(SPRelationDto spRelationDto) {
        initFlowRules();
        Entry entry = null;
        try {
            // 流控代码
            entry = SphU.entry(REL_RES);
            // 业务代码
            SPRelation spRelation = spRelationMapper.toModel(spRelationDto);
            spRelation.setCreatedDate(LocalDateTime.now());
            spRelation.setLastModifiedDate(LocalDateTime.now());
            return spRelationReactiveRepository.save(spRelation).map(s -> {
                        spRelationDto.setId(s.getId());
                        return spRelationDto;
                    }
            );
        }catch(BlockException e){
            // 被限流了
            System.out.println("[saveSPRelation] has been protected! Time="+System.currentTimeMillis());
        }finally {
            if(entry!=null){
                entry.exit();
            }
        }
        return null;
    }

    @Override
    public Flux<SPRelationDto> findAllSPRelations() {
        return spRelationReactiveRepository.findAll()
                .map(spRelationMapper::toDto)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<SPRelationDto> update(SPRelationDto spRelationDto, String id) {
        return spRelationReactiveRepository.findById(id)
                .flatMap(existingSPRelation -> {
                    SPRelation spRelation = spRelationMapper.toModel(spRelationDto);
                    spRelation.setId(existingSPRelation.getId());
                    return spRelationReactiveRepository.save(spRelation);
                })
                .map(spRelationMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return spRelationReactiveRepository.deleteById(id);
    }
}
