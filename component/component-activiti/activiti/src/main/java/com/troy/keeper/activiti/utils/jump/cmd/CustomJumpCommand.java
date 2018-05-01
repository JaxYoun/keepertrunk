package com.troy.keeper.activiti.utils.jump.cmd;

import com.troy.keeper.activiti.web.rest.errors.TroyActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Date:     2017/7/26 14:25<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class CustomJumpCommand  implements Command<Void> {
    protected String executionId;
    protected List<String> activityIds;
    protected Map<String, Object> taskValues;


    public CustomJumpCommand(String executionId, List<String> activityIds, Map<String, Object> taskValues) {
        this.executionId = executionId;
        this.activityIds = activityIds;
        this.taskValues = taskValues;
    }

    public Void execute(CommandContext commandContext) {
        if(CollectionUtils.isEmpty(activityIds)){
            throw new TroyActivitiException("no target activityIds");
        }
        ExecutionEntity execution = commandContext.getExecutionEntityManager().findExecutionById(executionId);
        execution.destroyScope("jump");
        ProcessDefinitionImpl processDefinition = execution.getProcessDefinition();
        if(activityIds.size() == 1){
            ActivityImpl findActivity = processDefinition.findActivity(activityIds.get(0));
            execution.setVariables(taskValues);
            execution.executeActivity(findActivity);
        } else {
            // 分支
            for (String activityId : activityIds) {
                ActivityImpl findActivity = processDefinition.findActivity(activityId);
                ExecutionEntity subExecution = execution.createExecution();
                subExecution.setVariables(taskValues);
                subExecution.executeActivity(findActivity);
            }
        }
        return null;
    }
}
