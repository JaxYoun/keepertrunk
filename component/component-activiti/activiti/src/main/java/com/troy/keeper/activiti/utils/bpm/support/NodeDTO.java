package com.troy.keeper.activiti.utils.bpm.support;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qjq
 * @version V1.0
 * @Title: NodeDTO
 * @Description:
 * @Company: troy
 * @date 2016年6月14日上午10:12:27
 */
public class NodeDTO {
    private int x;

    private int y;

    private int width;

    private int height;

    private String type;

    private String id;

    private String name;

    private String assignee;

    private String assigneeName;

    private String startTime;

    private String endTime;

    private List<EdgeDTO> outgoings = new ArrayList<>();

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<EdgeDTO> getOutgoings() {
        return outgoings;
    }

    public void setOutgoings(List<EdgeDTO> outgoings) {
        this.outgoings = outgoings;
    }
}
