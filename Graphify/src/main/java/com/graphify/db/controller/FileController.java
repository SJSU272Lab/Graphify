package com.graphify.db.controller;

import com.graphify.db.client.LocalClient;
import com.graphify.db.client.ServiceClient;
import com.graphify.db.model.ibm.graph.GraphSchema;
import com.graphify.db.model.mysql.Validate;
import com.graphify.db.util.DatabaseCredentials;
import com.graphify.db.util.ZipUtil;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

@Controller
public class FileController {
    private Logger logger = Logger.getLogger(FileController.class);
    @Autowired
    private DatabaseCredentials databaseCredentials;

    @RequestMapping(value = "/validate", method = RequestMethod.POST, produces = {"application/json"})
    public @ResponseBody HashMap<String, Object> validate(MultipartHttpServletRequest request,
                                                          HttpServletResponse response) throws Exception {
        logger.info("In controller");
        MultipartFile multipartFile = request.getFile("file");
        Long size = multipartFile.getSize();
        String contentType = multipartFile.getContentType();
        InputStream stream = multipartFile.getInputStream();
        File file;
        if(System.getProperty("os.name").contains("Windows")) {
            file = new File("D:\\" + multipartFile.getOriginalFilename());
        }
        else {
            file = new File("/usr/" + multipartFile.getOriginalFilename());
        }
        multipartFile.transferTo(file);
        logger.info("file "+ file.getAbsolutePath());


        String host = request.getParameter("host");
        String port = request.getParameter("port");
        String user = request.getParameter("user");
        String pass = request.getParameter("password");
        String db = request.getParameter("db");

        String url = "jdbc:mysql://%s:%s/%s?user=%s&password=%s&autoReconnect=true&useSSL=false";
        url = String.format(url, host, port, db, user, pass);

        logger.info(url +" file "+ file.getAbsolutePath());
        String outputDir;
        if(System.getProperty("os.name").contains("Windows"))
            outputDir = "D:\\readDir\\"+ Long.toString(System.currentTimeMillis());
        else
            outputDir = "/usr/readDir/"+ Long.toString(System.currentTimeMillis());

        ZipUtil.unZipIt(file.getAbsolutePath(), outputDir);

        databaseCredentials.setHost(request.getParameter("host"));
        databaseCredentials.setPort(request.getParameter("port"));
        databaseCredentials.setUser(request.getParameter("user"));
        databaseCredentials.setPass(request.getParameter("password"));
        databaseCredentials.setDb(request.getParameter("db"));
        databaseCredentials.setUrl(url);
        databaseCredentials.setOutputDir(outputDir);

        /*ServiceClient serviceClient = new ServiceClient("localhost", 8081, db);
        Validate validate = serviceClient.validate(url, outputDir);*/

        LocalClient localClient = new LocalClient();
        Validate validate = localClient.validate(url, outputDir, db);

        logger.info(validate);
        byte[] bytes = IOUtils.toByteArray(stream);

        HashMap<String, Object> map = new HashMap<>();
        map.put("fileoriginalsize", size);
        map.put("contenttype", contentType);
        map.put("base64", new String(Base64Utils.encode(bytes)));
        map.put("message", validate.getMessage());
        map.put("isValid", validate.isValid());

        return map;
    }

    @RequestMapping(value = "/conadd", method = RequestMethod.POST, produces = {"application/json"})
    public @ResponseBody HashMap<String, Object> convert(HttpServletRequest request,
                                                          HttpServletResponse response) throws Exception {

        LocalClient localClient = new LocalClient();
        GraphSchema graphSchema = localClient.conAdd(databaseCredentials.getUrl(), databaseCredentials.getOutputDir(), databaseCredentials.getDb());

        logger.info(graphSchema);


        HashMap<String, Object> map = new HashMap<>();
        map.put("contenttype",  request.getContentType());

        return map;
    }
}