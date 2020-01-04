package com.lhstack.controller;

import com.lhstack.entity.fastdfs.FastResult;
import com.lhstack.utils.FastDfsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("file")
public class FileUploadController {

    @Autowired
    private FastDfsUtils fastDfsUtils;

    @PostMapping("upload")
    public Mono<ResponseEntity<FastResult>> upload(@RequestPart("file")FilePart file) throws Exception {
        return Mono.just(ResponseEntity.ok(fastDfsUtils.uploadFile(file)));
    }

}
