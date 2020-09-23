package org.hzero.platform.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import org.hzero.platform.domain.entity.DashboardRoleCard;

import java.util.List;

/**
 * 角色卡片表资源库
 *
 * @author xiaoyu.zhao@hand-china.com 2019-01-24 19:58:17
 */
public interface DashboardRoleCardRepository extends BaseRepository<DashboardRoleCard> {

    /**
     * 查询角色卡片列表
     *
     * @param dashboardRoleCard 角色卡片实体
     * @param pageRequest 分页参数
     * @return Page<DashboardRoleCard>
     */
    Page<DashboardRoleCard> getRoleCardList(DashboardRoleCard dashboardRoleCard, PageRequest pageRequest);

    /**
     * 查询当前角色可分配的列表
     *
     * @param dashboardRoleCard 角色卡片实体
     * @param pageRequest 分页参数
     * @return Page<DashboardRoleCard>
     */
    Page<DashboardRoleCard> selectRoleAssignCard(DashboardRoleCard dashboardRoleCard, PageRequest pageRequest);

    /**
     * 查询获取平台/租户超级管理员id并组装为DashboardRoleCard实体
     */
    List<Long> selectSuperRoleIds();

    /**
     * 查询所有当前角色的父级角色上设置默认的数据
     *
     * @param parentRoleList 父级Id集合
     * @return 所有数据
     */
    List<DashboardRoleCard> selectByRoleIds(List<Long> parentRoleList);

    /**
     * 查询获取当前角色下设置的初始化卡片信息
     *
     * @param roleId 角色Id
     * @return 初始化数据
     */
    List<DashboardRoleCard> selectCurrentRoleCards(Long roleId);

    /**
     * 查询角色租户Id信息
     *
     * @param roleId 角色Id标识
     * @return 角色租户Id
     */
    Long selectRoleTenant(Long roleId);
}
