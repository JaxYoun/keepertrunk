package com.troy.keeper.monomer.portal.account.repository;


import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.monomer.portal.account.domain.Custom;
import com.troy.keeper.monomer.portal.account.domain.DataDictionary;
import com.troy.keeper.monomer.portal.account.domain.projection.CustomProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by yg on 2017/6/8.
 */
public interface CustomRepository extends BaseRepository<Custom, Serializable> {

    @Query("select c from Custom c where c.detail like %?1%")
    Custom queryCustomByDetail(String detail);

    @Query("select c from Custom c where c.detail like %?1%")
    CustomProjection queryCustomByDetail2(String detail);

//    @EntityGraph(attributePaths = {"addresses"})
    //默认都不级联fetch对象
    CustomProjection findByCustId(Long id);


    @Query("select c from Custom c")
    Stream<Custom> streamAllPaged(Pageable pageable);

    Custom findFirstByOrderByDetailAsc();

    Custom findTopByOrderByDetailDesc();

    Page<Custom> queryFirst10ByDetail(String detail, Pageable pageable);

    Slice<Custom> findTop3ByDetail(String detail, Pageable pageable);

    List<Custom> findFirst10ByDetail(String detail, Sort sort);

    List<Custom> findTop10ByDetail(String detail, Pageable pageable);

    Integer hello();

}
