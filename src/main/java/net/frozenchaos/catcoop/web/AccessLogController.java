package net.frozenchaos.catcoop.web;

import net.frozenchaos.catcoop.data.AccessRecord;
import net.frozenchaos.catcoop.data.AccessRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/accesslog")
public class AccessLogController {
    private static final String ACCESS_LOG_LIST_TEMPLATE = "accessLogList";
    private static final String ACCESS_LOG_RECORD_TEMPLATE = "accessLogRecord";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AccessRecordRepository accessRecordRepository;

    @Autowired
    public AccessLogController(AccessRecordRepository accessRecordRepository) {
        logger.info("Initializing AccessLogController");
        this.accessRecordRepository = accessRecordRepository;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String mainPage(ModelMap model) {
        Iterable<AccessRecord> records = accessRecordRepository.findAll();
        model.put("records", records);
        return ACCESS_LOG_LIST_TEMPLATE;
    }

    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public String accessRecord(@RequestParam(value = "id") String id, ModelMap model) {
        AccessRecord accessRecord = accessRecordRepository.findOne(Integer.valueOf(id));
        logger.trace("AccessRecord number (" + accessRecord.getTimestamp() + ", " + accessRecord.getCat() + " has this many images: " + accessRecord.getImages().size());
        model.put("record", accessRecord);
        return ACCESS_LOG_RECORD_TEMPLATE;
    }
}
