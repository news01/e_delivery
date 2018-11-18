/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.archive.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.archive.entity.Archives;
/**
 * 测试DAO接口
 * @author 朱明军
 * @version 2017-06-02
 */
@MyBatisDao
public interface ArchivesDao extends CrudDao<Archives> {
	public List<Archives> findMoHu(@Param("caseNum") String caseNum);
	public List<Archives> findByCaseNum(@Param("caseNum") String caseNum);
	public void deleteArchives(@Param("id") String id);
}