package net.frozenchaos.catcoop.web;

import net.frozenchaos.catcoop.configuration.Setting;
import net.frozenchaos.catcoop.configuration.SettingsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/configuration")
public class ConfigurationController {
    private static final String CONFIGURATION_PANEL = "configurationPanel";

    private SettingsManager settingsManager;

    @Autowired
    public ConfigurationController(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String configurationPanel(ModelMap model) {
        model.put("settings", Setting.values());
        return CONFIGURATION_PANEL;
    }
}
