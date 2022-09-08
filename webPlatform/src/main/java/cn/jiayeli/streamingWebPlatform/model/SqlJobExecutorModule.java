package cn.jiayeli.streamingWebPlatform.model;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Validated
public class SqlJobExecutorModule implements Serializable {

    @NotBlank(message = "jobName name dot is null")
    private String jobName;

    @NotBlank(message = "sqlScript dot is null")
    private String sqlScript;

    private String clusterId;

    private String module;

    private String dbName;

    private String taleName;

    public SqlJobExecutorModule() {
    }

    public SqlJobExecutorModule(String taskName, String sqlScript, String clusterId, String module, String dbName, String taleName) {
        this.jobName = taskName;
        this.sqlScript = sqlScript;
        this.clusterId = clusterId;
        this.module = module;
        this.dbName = dbName;
        this.taleName = taleName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getSqlScript() {
        return sqlScript;
    }

    public void setSqlScript(String sqlScript) {
        this.sqlScript = sqlScript;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTaleName() {
        return taleName;
    }

    public void setTaleName(String taleName) {
        this.taleName = taleName;
    }
}
