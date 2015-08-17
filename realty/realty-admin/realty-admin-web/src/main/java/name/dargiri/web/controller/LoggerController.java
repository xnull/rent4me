package name.dargiri.web.controller;

//import name.dargiri.data.dto.PersonDTO;
//import name.dargiri.data.service.PersonService;

import bynull.realty.dto.CityDTO;
import bynull.realty.dto.CountryDTO;
import bynull.realty.services.api.CityService;
import bynull.realty.services.api.CountryService;
import name.dargiri.web.Constants;
import name.dargiri.web.converters.CityAdminConverter;
import name.dargiri.web.converters.CountryAdminConverter;
import name.dargiri.web.form.BoundingBoxForm;
import name.dargiri.web.form.CityForm;
import name.dargiri.web.form.CountryForm;
import name.dargiri.web.form.GeoPointForm;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dionis on 2/3/14.
 */

@Controller
@RequestMapping("/secure/logger")
public class LoggerController {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private LogReader logReader;

    @RequestMapping(value = "log", method = RequestMethod.GET)
    public String getLog(
            Model model,
            @RequestParam(value = "direction", required = false, defaultValue = LogReader.BACK_DIRECTION) String direction)
            throws IOException {

        String fileContent = logReader.getLog(direction);
        model.addAttribute("logger", fileContent);
        return "logs/logger";
    }

    @RequestMapping(value = "log-analyser", method = RequestMethod.GET)
    public String logAnalyser(Model model, @RequestParam(value = "regex", required = false) String regEx) throws IOException{
        String fileContent = logReader.getLog("forward");

        Matcher matcher = Pattern.compile(regEx).matcher(fileContent);
        MultiLine result = new MultiLine();
        while (matcher.find()) {
            result.addLine(matcher.group());
        }

        model.addAttribute("logger", result.toString());
        return "logs/logger";
    }

    @Component
    public static class LogReader {

        public static final String BACK_DIRECTION = "back";

        private String fileContent;
        private long checkSumm;

        private final Object syncObject = new Object();

        public String getLog(String direction) throws IOException {

            synchronized (syncObject) {

                // String catalogPath = System.getProperty("catalina.base");
                File logFile = getLogFile();

                // if (logIsModified(logFile)) {
                // model.addAttribute("logger", fileContent);
                // return "logger";
                // }

                fillFileContent(logFile, direction);

                return fileContent;
            }
        }

        private void fillFileContent(File logFile, String direction) throws IOException {
            fileContent = FileUtils.readFileToString(logFile, "UTF-8");

            /*List<String> result = FileUtils.readLines(logFile, "UTF-8");
            StringBuilder resultStr = new StringBuilder();

            boolean isBack = direction.equals(BACK_DIRECTION);

            Long currLine = 1L;
            if (isBack) {
                Collections.reverse(result);
                currLine = (long) result.size();
            }

            for (String string : result) {
                resultStr.append(currLine).append("  ").append(string).append("\n");
                currLine = getNextNum(currLine, isBack);
            }

            fileContent = resultStr.toString();*/
        }

        private static long getNextNum(Long i, boolean isBack) {

            if (isBack) {
                return i - 1;
            }

            return i + 1;
        }

        private boolean logIsModified(File logFile) throws IOException {
            long currCheckSumm = FileUtils.checksumCRC32(logFile);
            if (currCheckSumm == checkSumm) {
                return false;
            }

            checkSumm = currCheckSumm;
            return true;
        }

        private File getLogFile() {
            String catalogPath = System.getProperty("catalina.home");
            String logPath = catalogPath + File.separator + "logs" + File.separator + "catalina.out";

            return new File(logPath);
        }
    }

    public static class MultiLine {

        java.util.List<String> multiLine = new ArrayList<>();

        public void addLine(String line) {
            multiLine.add(line);
        }

        public void addLineFormat(String lineFormat, Object... args) {
            multiLine.add(MessageFormat.format(lineFormat, args));
        }

        @Override
        public String toString() {
            return StringUtils.join(multiLine, "\n");
        }
    }
}
