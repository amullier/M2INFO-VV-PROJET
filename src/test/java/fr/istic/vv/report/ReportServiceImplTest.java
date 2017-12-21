package fr.istic.vv.report;

import fr.istic.vv.common.MutantContainer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReportServiceImplTest {

    ReportServiceImpl reportService;

    MutantContainer mutantContainer;

    Report report;


    @Before
    public void setup(){
        reportService = new ReportServiceImpl();

        mutantContainer = mock(MutantContainer.class);
        mutantContainer.setMutatedClass("CLASS");
        mutantContainer.setMutationMethod("METHOD");
        mutantContainer.setMutationType(MutantContainer.MutantType.ADDITION);

        verify(mutantContainer).setMutatedClass("CLASS");
        verify(mutantContainer).setMutationMethod("METHOD");
        verify(mutantContainer).setMutationType(MutantContainer.MutantType.ADDITION);


        report = mock(Report.class);
        when(report.getMutantContainer()).thenReturn(mutantContainer);

    }

    @Test
    public void addANullReport(){
        reportService.addReport(null);
    }

    @Test
    public void addAReportWithNullMutantContainer(){
        Report report = mock(Report.class);
        report.setMutantContainer(null);
        reportService.addReport(report);
        verify(report).setMutantContainer(null);
    }

    @Test
    public void addASampleReport(){

        int before  = reportService.getTotalNumber();
        reportService.addReport(report);

        assertEquals(before+1,reportService.getTotalNumber());
        assertEquals(before+1,reportService.getReports().size());

    }

    @Test
    public void addAliveMutantReport(){

        int before  = reportService.getTotalNumber();
        int beforeAlive  = reportService.getAliveNumber();

        when(report.isMutantAlive()).thenReturn(true);
        reportService.addReport(report);

        assertEquals(before+1,reportService.getTotalNumber());
        assertEquals(before+1,reportService.getReports().size());
        assertEquals(beforeAlive+1,reportService.getAliveNumber());

    }

    @Test
    public void addDeadMutantReport(){

        int before  = reportService.getTotalNumber();
        int beforeAlive  = reportService.getAliveNumber();

        when(report.isMutantAlive()).thenReturn(false);
        reportService.addReport(report);

        assertEquals(before+1,reportService.getTotalNumber());
        assertEquals(before+1,reportService.getReports().size());
        assertEquals(beforeAlive,reportService.getAliveNumber());
    }

    @Test
    public void generateReports(){
        when(report.isMutantAlive()).thenReturn(false);
        reportService.addReport(report);
        reportService.addReport(report);
        reportService.addReport(report);

        reportService.setStartTesting(0);
        reportService.setStopTesting(1000000000);

        assertEquals(3,reportService.getTotalNumber());

        assertTrue(reportService.toCSV().length()>0);
        assertTrue(reportService.toHTML().length()>0);
        System.out.println(reportService.toHTML());

    }
}