package com.troy.keeper.monomer.api.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

/**
 * Description: 租户资源统计， 管理员为平台资源<br/>
 * Date:     2017/4/6 11:04<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class PlatResourcesDTO extends BaseDTO {
    /**
     * 内存信息
     */
    private MemoDTO memo = null;
    /**
     * cpu信息
     */
    private CpuDTO cpu = null;
    /**
     * 磁盘使用信息
     */
    private DiskDTO disk = null;

    public MemoDTO getMemo() {
        return memo;
    }

    public void setMemo(MemoDTO memo) {
        this.memo = memo;
    }

    public CpuDTO getCpu() {
        return cpu;
    }

    public void setCpu(CpuDTO cpu) {
        this.cpu = cpu;
    }

    public DiskDTO getDisk() {
        return disk;
    }

    public void setDisk(DiskDTO disk) {
        this.disk = disk;
    }
}
