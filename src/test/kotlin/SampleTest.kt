import org.example.Sample
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class SampleTest {

    @Test
    fun addTest() {
        var sampleTest = Sample()
        assertEquals(4, sampleTest.add(1,2))
    }
}