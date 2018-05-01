package com.troy.keeper.core.config;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Properties specific to JHipster.
 * <p/>
 * <p>
 * Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "cloud", ignoreUnknownFields = false)
public class CloudProperties {

    private final Login login = new Login();

    private final App app = new App();

    private final Performance performance = new Performance();

    private static Environment env;

    public Login getLogin() {
        return login;
    }

    public App getApp() {
        return app;
    }

    public Performance getPerformance() {
        return performance;
    }

    public static class App {

        private final Combine combine = new Combine();

        public Combine getCombine() {
            return combine;
        }

        public static class Combine {

            private static List<String> combinations = new ArrayList<>();

            private static String host = "localhost";

            private static String address;

            private int port;

            private boolean enabled=false;

            public static void init(Environment env) {
                int port = env.getProperty("server.port", Integer.class, -1);
                int appPort = env.getProperty("cloud.app.combine.port", Integer.class, -1);
                String host = env.getProperty("cloud.app.combine.host",String.class, null);
                List<String> combinations = env.getProperty("cloud.app.combine.combinations",List.class, null);
                if (null!=combinations) {
                    Combine.combinations = combinations;
                    Validate.isTrue(port > 0, "server.port haven't configure!");
                    port = appPort>0?appPort:port;
                    host= host==null?Combine.host:host;
                    address = "http://"+host+":"+port;
                }
            }

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public static  String address() {
                return address;
            }

            public static boolean isCombine(String name) {
                return combinations.contains(name);
            }
            public List<String> getCombinations() {
                return combinations;
            }

            public void setCombinations(List<String> combinations) {
                this.combinations = combinations;
            }
        }

    }

    public static class Login {

        private String successLoginUrl = "/";

        /**
         * login page url
         */
        private String loginFormUrl = "/login";

        /**
         * access failure without login Url
         */
        private String failureUrl = "/login?error";

        public String getFailureUrl() {
            return failureUrl;
        }

        public void setFailureUrl(String failureUrl) {
            this.failureUrl = failureUrl;
        }

        public String getLoginFormUrl() {
            return loginFormUrl;
        }

        public void setLoginFormUrl(String loginFormUrl) {
            this.loginFormUrl = loginFormUrl;
        }

        public String getSuccessLoginUrl() {
            return successLoginUrl;
        }

        public void setSuccessLoginUrl(String successLoginUrl) {
            this.successLoginUrl = successLoginUrl;
        }
    }

    public static class Performance {

        private final Check check = new Check();

        public Check getCheck() {
            return check;
        }

        public class Check {

            private boolean enabled=true;

            private short seconds=2;

            private List<String> excludes = Lists.newArrayList();

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public short getSeconds() {
                return seconds;
            }

            public void setSeconds(short seconds) {
                this.seconds = seconds;
            }

            public List<String> getExcludes() {
                return excludes;
            }

            public void setExcludes(List<String> excludes) {
                this.excludes = excludes;
            }
        }
    }
}
