package com.yiyang.profile.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yiyang.common.annotation.Log;
import com.yiyang.common.core.controller.BaseController;
import com.yiyang.common.core.domain.AjaxResult;
import com.yiyang.common.enums.BusinessType;
import com.yiyang.profile.domain.UserProfile;
import com.yiyang.profile.service.IUserProfileService;
import com.yiyang.common.utils.poi.ExcelUtil;
import com.yiyang.common.core.page.TableDataInfo;

/**
 * 用户扩展信息Controller
 * 
 * @author GuangWenLi
 * @date 2025-12-18
 */
@RestController
@RequestMapping("/profile/profile")
public class UserProfileController extends BaseController
{
    @Autowired
    private IUserProfileService userProfileService;

    /**
     * 查询用户扩展信息列表
     */
    @PreAuthorize("@ss.hasPermi('profile:profile:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserProfile userProfile)
    {
        startPage();
        List<UserProfile> list = userProfileService.selectUserProfileList(userProfile);
        return getDataTable(list);
    }

    /**
     * 导出用户扩展信息列表
     */
    @PreAuthorize("@ss.hasPermi('profile:profile:export')")
    @Log(title = "用户扩展信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserProfile userProfile)
    {
        List<UserProfile> list = userProfileService.selectUserProfileList(userProfile);
        ExcelUtil<UserProfile> util = new ExcelUtil<UserProfile>(UserProfile.class);
        util.exportExcel(response, list, "用户扩展信息数据");
    }

    /**
     * 获取用户扩展信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('profile:profile:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(userProfileService.selectUserProfileByUserId(userId));
    }

    /**
     * 新增用户扩展信息
     */
    @PreAuthorize("@ss.hasPermi('profile:profile:add')")
    @Log(title = "用户扩展信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserProfile userProfile)
    {
        return toAjax(userProfileService.insertUserProfile(userProfile));
    }

    /**
     * 修改用户扩展信息
     */
    @PreAuthorize("@ss.hasPermi('profile:profile:edit')")
    @Log(title = "用户扩展信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserProfile userProfile)
    {
        return toAjax(userProfileService.updateUserProfile(userProfile));
    }

    /**
     * 删除用户扩展信息
     */
    @PreAuthorize("@ss.hasPermi('profile:profile:remove')")
    @Log(title = "用户扩展信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(userProfileService.deleteUserProfileByUserIds(userIds));
    }
}
