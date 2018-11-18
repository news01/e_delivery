/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.archivetrank.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.archivetrank.entity.ArchivesTrank;

/**
 * 测试DAO接口
 * @author 朱明军
 * @version 2017-06-02
 */
@MyBatisDao
public interface ArchivesTrankDao extends CrudDao<ArchivesTrank> {
	public ArchivesTrank findLastCircul(@Param("file_id") String file_id);
	public void updateTurnTime(@Param("turnTime") String turnTime,@Param("id") String id);
	public List<ArchivesTrank> findMyFile(@Param("id") String id,@Param("upTime") String upTime,@Param("lowTime") String lowTime,@Param("start") int start,@Param("end") int end);
	public List<ArchivesTrank> findMyTurn(@Param("id") String id,@Param("upTime") String upTime,@Param("lowTime") String lowTime,@Param("start") int start,@Param("end") int end);
	public List<ArchivesTrank> findMyTurn2(@Param("id") String id,@Param("upTime") String upTime,@Param("lowTime") String lowTime);
	public List<ArchivesTrank> findByFileId(@Param("id") String id,@Param("start") int start,@Param("end") int end);
	public ArchivesTrank findTurnTo(@Param("fileId") String fileId,@Param("signTime") String signTime);
	public List<ArchivesTrank> findNowHave(@Param("id") String id,@Param("start") int start,@Param("end") int end);
	public List<ArchivesTrank> findAllNowHave(@Param("id") String id);
	public List<ArchivesTrank> findCount();
	public List<ArchivesTrank> findAllHave(@Param("id") String id);
	public void deleteByFileId(@Param("file_id") String file_id);
}