package com.boot.cms.dto.Convent;



import com.boot.cms.dto.FileDTO;
import com.boot.cms.pojo.File;
import org.mapstruct.factory.Mappers;

import java.util.List;

@org.mapstruct.Mapper
public interface FileConvert {
    FileConvert MAPPER = Mappers.getMapper(FileConvert.class);

    public FileDTO do2dto(File person);

    public List<FileDTO> dos2dtos(List<File> list);
}