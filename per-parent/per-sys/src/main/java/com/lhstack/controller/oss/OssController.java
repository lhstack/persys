package com.lhstack.controller.oss;

import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.entity.fastdfs.FastResult;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.layui.LayuiTableResult;
import com.lhstack.entity.oss.OssEntity;
import com.lhstack.service.oss.IOssService;
import com.lhstack.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("oss")
public class OssController {

    @Autowired
    private IOssService ossService;


    /**
     * 删除多文件
     * @param token
     * @param keys 可以为全路径或者文件名加后缀
     * @return
     * @throws Exception
     */
    @DeleteMapping("deletes")
    public ResponseEntity<LayuiResut<Boolean>> deleteObjects(
            @RequestParam("token") String token,
            @RequestParam("keys") List<String> keys) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(ossService.deleteObjects(token,keys)).setMsg("删除成功"));
    }

    @DeleteMapping("delete")
    public ResponseEntity<LayuiResut<Boolean>> deleteObject(
            @RequestParam("token") String token,
            @RequestParam("key") String key) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(ossService.deleteObject(token,key)).setMsg("删除成功"));
    }


    @PostMapping("upload")
    public ResponseEntity<LayuiResut<FastResult>> upload(@RequestParam("token") String token,
                                                         @RequestPart(value = "file") FilePart filePart,
                                                         @RequestParam(value = "directory",defaultValue = "") String directory,
                                                         @RequestParam(value = "generator_type",defaultValue = "uuid") String generatorType) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(ossService.upload(generatorType, FileUtils.filePartToBytes(filePart), filePart.filename(),token,directory)).setMsg("上传文件成功"));
    }


    @PostMapping("multi/upload")
    public ResponseEntity<LayuiTableResult<FastResult>> multiUpload(@RequestParam("token") String token,
                                                         @RequestPart(value = "files") Flux<FilePart> fileParts,
                                                         @RequestParam(value = "directory",defaultValue = "") String directory,
                                                         @RequestParam(value = "generator_type",defaultValue = "uuid") String generatorType) throws Exception {
        Map<String, byte[]> mappingContents = FileUtils.toMapFileMappingContents(fileParts);
        List<FastResult> fastResults = ossService.uploadMultiFile(generatorType, mappingContents, token,directory);
        LayuiTableResult<FastResult> fastResultLayuiTableResult = new LayuiTableResult<>();
        fastResultLayuiTableResult.setCount((long) fastResults.size())
                .setCode(200)
                .setMsg("上传多文件成功")
                .setData(fastResults);
        return ResponseEntity.ok(fastResultLayuiTableResult);
    }

    @PostMapping("add")
    @DynAuthority
    public ResponseEntity<LayuiResut<OssEntity>> save(@RequestBody OssEntity ossEntity) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(ossService.save(ossEntity)).setMsg("添加成功"));
    }

    @PutMapping("update/{id}")
    @DynAuthority
    public ResponseEntity<LayuiResut<OssEntity>> update(@PathVariable("id") String id,@RequestBody OssEntity ossEntity) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(ossService.update(id,ossEntity)).setMsg("更新成功"));
    }

    @DeleteMapping("del/{id}")
    @DynAuthority
    public ResponseEntity<LayuiResut<Boolean>> del(@PathVariable("id") String id) throws Exception {
        ossService.deleteById(id);
        return ResponseEntity.ok(LayuiResut.buildSuccess(true).setMsg("删除成功"));
    }

    @GetMapping("list")
    @DynAuthority
    public ResponseEntity<LayuiTableResult<OssEntity>> list(@RequestParam(value = "page",defaultValue = "1") Integer page,@RequestParam(value = "size",defaultValue = "10") Integer size) throws Exception {
        Page<OssEntity> ossEntityPage = ossService.findAll(page, size);
        LayuiTableResult<OssEntity> layuiTableResult = new LayuiTableResult<>();
        layuiTableResult.setCount(ossEntityPage.getTotalElements())
                .setCode(200)
                .setMsg("查询成功")
                .setData(ossEntityPage.getContent());
        return ResponseEntity.ok(layuiTableResult);
    }
}
