package com.troy.keeper.fileupload.web.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;

import com.troy.keeper.fileupload.dto.FileTypeDTO;
import com.troy.keeper.fileupload.service.FileTypeService;


import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class FileTypeResource extends BaseResource<FileTypeDTO> {
    @Autowired
    private FileTypeService fileTypeService;

    // 默认分页条数
    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 文件类型查询
     *
     * @return
     */
    @RequestMapping(value = "/api/upload/fileType/list", method = RequestMethod.POST)
    public ResponseDTO<List<FileTypeDTO>> list(@RequestBody FileTypeDTO fileTypeDTO) throws Exception {
            return success(fileTypeService.list(fileTypeDTO));
    }

    /**
     * 分页查询文件类型
     * @param
     * @return
     */
    @RequestMapping(value = "/api/upload/fileType/listForPage", method = RequestMethod.POST)
    public ResponseDTO<Page<FileTypeDTO>> listForPage(@RequestBody FileTypeDTO fileTypeDTO) throws Exception {
        return success(fileTypeService.listForPage(fileTypeDTO));
    }

    /**
     * 查询指文件类型
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/api/upload/fileType/get", method = RequestMethod.POST)
    public ResponseDTO<FileTypeDTO> get(@RequestBody Long id) throws Exception {
        Validate.notNull(id,"can't be empty!");
//        if(id == null || id == 0l){
//          return fail("id未传入");
//        }
       return success(fileTypeService.get(id));
    }

    /**
     * 保存文件分类
     * @param
     * @return
     */
    @RequestMapping(value = "/api/upload/fileType/save", method = RequestMethod.POST)
    public ResponseDTO<FileTypeDTO> save(@RequestBody FileTypeDTO fileTypeDTO) throws Exception {
        Validate.notNull(fileTypeDTO.getCategoryCode(),"can't be empty!");
       return success(fileTypeService.save(fileTypeDTO));
    }

    /**
     * 删文件分类单接口
     * @param
     * @return
     */
    @RequestMapping(value = "/api/upload/fileType/del", method = RequestMethod.POST)
    public ResponseDTO<?> del(@RequestBody FileTypeDTO fileTypeDTO) throws Exception {
        Validate.notNull(fileTypeDTO.getId());
        fileTypeService.del(fileTypeDTO.getId());
        return success();
    }

    /**
     * 根据分类id查询文件
     * @param
     * @return
     */
    @RequestMapping(value = "/api/upload/fileType/qryFilesByTypeId", method = RequestMethod.POST)
    public ResponseDTO<?> qryFilesByTypeId(@RequestBody FileTypeDTO dto) throws Exception {
        Validate.notNull(dto.getId());
        return success(fileTypeService.queryFileByTypeId(dto));
    }
}
