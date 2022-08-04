package com.boot.cms.service.impl;

import com.boot.cms.dao.FileDao;
import com.boot.cms.pojo.File;
import com.boot.cms.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;





@Service
public class FileServiceImpl implements FileService {
	@Autowired
	private FileDao fileDao;
	
	@Override
	public File get(Long id){
		return fileDao.get(id);
	}
	
	@Override
	public List<File> list(Map<String, Object> map){
		return fileDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return fileDao.count(map);
	}
	
	@Override
	public int save(File file){
		return fileDao.save(file);
	}
	
	@Override
	public int update(File file){
		return fileDao.update(file);
	}
	
	@Override
	public int remove(Long id){
		return fileDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return fileDao.batchRemove(ids);
	}
	
}
