/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.tuten.core.utils;

import java.util.UUID;

/**
 *
 * @author karol
 */
public class Security {
    private Long userId;
    private UUID tenantId;
    private String username;

    public Security(Long userId, UUID tenantId, String username) {
        this.userId = userId;
        this.tenantId = tenantId;
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
