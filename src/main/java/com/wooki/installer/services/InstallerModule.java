package com.wooki.installer.services;

import java.util.Properties;

import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.internal.services.ClasspathResourceSymbolProvider;
import org.apache.tapestry5.ioc.services.SymbolProvider;

import com.spreadthesource.tapestry.dbmigration.MigrationSymbolConstants;
import com.spreadthesource.tapestry.dbmigration.services.MigrationModule;
import com.spreadthesource.tapestry.installer.services.ApplicationSettings;
import com.wooki.core.services.CoreModule;

/**
 * Module for installation wizard application.
 */
@SubModule(
{ CoreModule.class, MigrationModule.class })
public class InstallerModule
{

    public void contributeApplicationDefaults(MappedConfiguration<String, String> conf)
    {
        conf.add(MigrationSymbolConstants.DEFAULT_HIBERNATE_CONFIGURATION, "false");
    }

    /**
     * Link migration helper to application settings service.
     * 
     * @param configurers
     * @param settings
     */
    public void contributeDbSource(OrderedConfiguration<HibernateConfigurer> configurers,
            final ApplicationSettings settings)
    {
        configurers.add("AppSettings", new HibernateConfigurer()
        {

            public void configure(org.hibernate.cfg.Configuration configuration)
            {
                configuration.setProperties(new Properties()
                {
                    @Override
                    public String getProperty(String key)
                    {
                        return settings.get(key);
                    }

                });

            }
        });
    }

    public static void contributeSymbolSource(OrderedConfiguration<SymbolProvider> providers)
    {
        providers.add("tapestryInstallerConfiguration", new ClasspathResourceSymbolProvider(
                "config/installer.properties"));
    }

    public void contributeMigrationManager(Configuration<String> configuration)
    {
        configuration.add("com.wooki.installer.schema");
    }
}
