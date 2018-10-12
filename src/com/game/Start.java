package com.game;


import com.game.event.ShutdownHandler;
import com.game.sdk.SdkServer;
import com.game.util.BeanManager;
import com.game.util.ConfigData;
import com.game.util.GameData;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Start {
    private static Logger logger = Logger.getLogger(Start.class);
    private static Start start = new Start();

    public static void main(String[] args) {
        start.init();
    }

    private void init() {
        try {
            SysConfig.init();
            DOMConfigurator.configure("config/log4j.xml");
            logger.info("begin init server...");

            logger.info("load game config...");
            GameData.loadConfigData();
            ConfigData.init();
            logger.info("load spring cfg...");
            ApplicationContext ctx = new FileSystemXmlApplicationContext("config/application.xml");
            BeanManager.onStart(ctx);

            Runtime.getRuntime().addShutdownHook(new ShutdownHandler());
            if (SysConfig.debug) {
                ResourceLeakDetector.setLevel(Level.PARANOID);
            }
            //延迟一些
            Thread.sleep(3000l);
            SdkServer.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
