package com.troy.keeper.activiti.utils.bpm.cmd;

import com.troy.keeper.activiti.utils.bpm.util.CustomProcessDiagramGenerator;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import java.io.InputStream;

public class HistoryProcessInstanceDiagramCmd implements Command<InputStream> {
    protected String historyProcessInstanceId;

    public HistoryProcessInstanceDiagramCmd(String historyProcessInstanceId) {
        this.historyProcessInstanceId = historyProcessInstanceId;
    }

    public InputStream execute(CommandContext commandContext) {
        try {
            CustomProcessDiagramGenerator customProcessDiagramGenerator = new CustomProcessDiagramGenerator();

            return customProcessDiagramGenerator
                    .generateDiagram(historyProcessInstanceId);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
