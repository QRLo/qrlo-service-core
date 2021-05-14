package com.qrlo.qrloservicecore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-07
 */
@Data
public class MissingProfile {
    private List<String> fields = new ArrayList<>();

    @JsonIgnore
    public boolean isEmpty() {
        return fields.isEmpty();
    }
}
