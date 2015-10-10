package net.minecraft.util;

import net.minecraft.crash.CrashReport;

public class ReportedException extends RuntimeException
{
    private final CrashReport theReportedExceptionCrashReport;
    private static final String __OBFID = "CL_00001579";

    public ReportedException(CrashReport report)
    {
        this.theReportedExceptionCrashReport = report;
    }

    public CrashReport getCrashReport()
    {
        return this.theReportedExceptionCrashReport;
    }

    public Throwable getCause()
    {
        return this.theReportedExceptionCrashReport.getCrashCause();
    }

    public String getMessage()
    {
        return this.theReportedExceptionCrashReport.getDescription();
    }
}