package com.apestech.scm.im.repository;

import com.apestech.scm.im.domain.Mvt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 功能：Mvt Repository
 *
 * @author xul
 * @create 2018-01-15 16:33
 */
public interface MvtRepository extends JpaRepository<Mvt, Integer>, JpaSpecificationExecutor<Mvt> {
}
