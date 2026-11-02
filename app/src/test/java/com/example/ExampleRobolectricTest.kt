package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.DefaultData
import com.example.data.model.SchedulePhase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("EduCore", appName)
    }

    @Test
    fun `verify seven subjects and units configured`() {
        val subjects = DefaultData.subjects
        assertEquals(7, subjects.size)

        val units = DefaultData.units
        assertEquals(35, units.size) // 7 subjects * 5 units

        // Check Financial Accounting units
        val faUnits = units.filter { it.subjectId == "FA" }
        assertEquals(5, faUnits.size)

        // Check Business Laws units
        val blUnits = units.filter { it.subjectId == "BL" }
        assertEquals(5, blUnits.size)

        // Check MPA units
        val mpaUnits = units.filter { it.subjectId == "MPA" }
        assertEquals(5, mpaUnits.size)
    }

    @Test
    fun `verify routine alarms count and schedule span`() {
        val alarms = DefaultData.alarms
        assertEquals(9, alarms.size)

        val tasks = DefaultData.scheduleTasks
        assertTrue(tasks.isNotEmpty())

        val firstTask = tasks.first()
        assertEquals("2026-11-03", firstTask.dateStr)

        val lastTask = tasks.last()
        assertEquals("2026-12-06", lastTask.dateStr)
        assertEquals(SchedulePhase.FINAL_PREPARATION, lastTask.phase)
    }
}
