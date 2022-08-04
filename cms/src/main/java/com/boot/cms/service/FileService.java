package com.boot.cms.service;



import com.boot.cms.pojo.File;

import java.util.List;
import java.util.Map;

/**
 * 文件上传
 */
public interface FileService {
	
	File get(Long id);
	
	List<File> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(File file);
	
	int update(File file);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
