package com.qrlo.qrloservicecore.user.store.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-04-30
 */
@Data
public class Store {
    @Id
    private final String id;
    private final String userId;
    private final GenericDataStore genericDataStore;
    private final BusinessCardStore businessCardStore;
}
