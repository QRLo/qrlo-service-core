package com.qrlo.qrloservicecore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

/**
 * @author rostradamus <rolee0429@gmail.com>
 * @date 2021-05-18
 */
@Configuration
@EnableR2dbcAuditing
@EnableR2dbcRepositories
public class DatabaseConfig {
}
