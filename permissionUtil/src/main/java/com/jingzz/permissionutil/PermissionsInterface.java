package com.jingzz.permissionutil;

import java.util.List;

/**
 * Created by Administrator - PC on 2017/11/21.
 */

public interface PermissionsInterface {
    /**
     * 通过全部权限
     */
    void allAgree();

    /**
     * 至少拒绝了一个权限
     */
    void refuse();


    /**
     * 被拒绝的权限
     * @param deniedPermissions
     */
    void refuse( List<String> deniedPermissions);

    /**
     * 被拒绝且选择了不再提示的权限
     * @param alwaysDeniedPermissions
     */
    void refusAndNotPrompt(List<String> alwaysDeniedPermissions);
}
