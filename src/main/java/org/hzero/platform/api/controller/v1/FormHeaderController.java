package org.hzero.platform.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.platform.app.service.FormHeaderService;
import org.hzero.platform.config.PlatformSwaggerApiConfig;
import org.hzero.platform.domain.entity.FormHeader;
import org.hzero.platform.domain.repository.FormHeaderRepository;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.CustomPageRequest;
import io.choerodon.swagger.annotation.Permission;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 表单配置头 管理 API
 *
 * @author xiaoyu.zhao@hand-china.com 2019-07-11 17:50:08
 */
@Api(tags = PlatformSwaggerApiConfig.FORM_HEADER)
@RestController("formHeaderController.v1")
@RequestMapping("/v1/{organizationId}/form-headers")
public class FormHeaderController extends BaseController {

    private final FormHeaderRepository formHeaderRepository;
    private final FormHeaderService formHeaderService;

    @Autowired
    public FormHeaderController(FormHeaderRepository formHeaderRepository, FormHeaderService formHeaderService) {
        this.formHeaderRepository = formHeaderRepository;
        this.formHeaderService = formHeaderService;
    }

    @ApiOperation(value = "表单配置头列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "formCode", value = "表单编码", paramType = "query"),
            @ApiImplicitParam(name = "formGroupCode", value = "表单归类", paramType = "query"),
            @ApiImplicitParam(name = "organizationId", value = "租户Id", paramType = "path", required = true),
            @ApiImplicitParam(name = "formName", value = "表单名称", paramType = "query")
    })
    @CustomPageRequest
    @GetMapping
    public ResponseEntity<Page<FormHeader>> pageFormHeaders(@PathVariable("organizationId") Long tenantId,
                    FormHeader formHeader, @ApiIgnore @SortDefault(value = FormHeader.FIELD_FORM_HEADER_ID,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        formHeader.setTenantId(tenantId);
        return Results.success(formHeaderRepository.pageFormHeaders(pageRequest, formHeader));
    }

    @ApiOperation(value = "表单配置头明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiImplicitParams({@ApiImplicitParam(name = "organizationId", value = "租户Id", paramType = "path", required = true),
            @ApiImplicitParam(name = "formHeaderId", value = "表单头Id", paramType = "path", required = true)
    })
    @GetMapping("/{formHeaderId}")
    public ResponseEntity<FormHeader> detail(@PathVariable @Encrypt Long formHeaderId) {
        return Results.success(formHeaderRepository.selectFormHeaderDetails(formHeaderId));
    }

    @ApiOperation(value = "创建表单配置头")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<FormHeader> create(@PathVariable("organizationId") Long tenantId,
                    @RequestBody FormHeader formHeader) {
        formHeader.setTenantId(tenantId);
        validObject(formHeader);
        return Results.success(formHeaderService.createFormHeader(formHeader));
    }

    @ApiOperation(value = "修改表单配置头")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<FormHeader> update(@PathVariable("organizationId") Long tenantId,
            @RequestBody @Encrypt FormHeader formHeader) {
        formHeader.setTenantId(tenantId);
        SecurityTokenHelper.validToken(formHeader);
        validObject(formHeader);
        return Results.success(formHeaderService.updateFormHeader(formHeader));
    }

    @ApiOperation(value = "删除表单配置头")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiImplicitParam(name = "organizationId", value = "租户Id", paramType = "path", required = true)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody @Encrypt FormHeader formHeader) {
        SecurityTokenHelper.validToken(formHeader);
        formHeaderService.deleteFormHeader(formHeader);
        return Results.success();
    }

    @ApiOperation(value = "查询启用的表单配置头列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "organizationId", value = "租户Id", paramType = "path", required = true)
    })
    @GetMapping("/enabled-list")
    public ResponseEntity<List<FormHeader>> listEnabledFormHeaders(@PathVariable("organizationId") Long tenantId) {
        return Results.success(formHeaderRepository.listEnabledFormHeaders(tenantId));
    }
}
