package mx.qsistemas.infracciones.alarm

import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.content.Intent
import android.os.Build
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.utils.*

class Alarms {

    init {
        setAlarmSendReports()
        setAlarmSendGeos()
    }

    private fun setAlarmSendReports() {
        /* Only send reports if user is reporter */
        val serviceComponent = ComponentName(Application.getContext(), ReportsService::class.java)
        val jobScheduler = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Application.getContext().getSystemService(JobScheduler::class.java)
        } else {
            Application.getContext().getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        }
        jobScheduler?.schedule(Utils.createJob(NOTIF_SEND_REPORTS, ALARM_SEND_REPORT_TIME, serviceComponent).build())
    }

    private fun setAlarmSendGeos() {
        val serviceComponent = ComponentName(Application.getContext(), GeosService::class.java)
        val jobScheduler = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Application.getContext().getSystemService(JobScheduler::class.java)
        } else {
            Application.getContext().getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        }
        jobScheduler?.schedule(Utils.createJob(NOTIF_SEND_GEOS, ALARM_SEND_GEOS_TIME, serviceComponent).build())
    }

    class SendReportAlarm : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            /* Only send reports if user is reporter */
            val serviceComponent = ComponentName(context, ReportsService::class.java)
            val jobScheduler = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.getSystemService(JobScheduler::class.java)
            } else {
                context.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            }
            jobScheduler?.schedule(Utils.createJob(NOTIF_SEND_REPORTS, ALARM_SEND_REPORT_TIME, serviceComponent).build())
        }
    }

    class SendGeosAlarm : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val serviceComponent = ComponentName(context, GeosService::class.java)
            val jobScheduler = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.getSystemService(JobScheduler::class.java)
            } else {
                context.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            }
            jobScheduler?.schedule(Utils.createJob(NOTIF_SEND_GEOS, ALARM_SEND_GEOS_TIME, serviceComponent).build())
        }
    }
}