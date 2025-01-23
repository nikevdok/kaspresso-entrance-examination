package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 20f)

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `addCereal should add cereal to existing container if space is available`() {
        val remaining = storage.addCereal(Cereal.WHEAT, 5f)
        assertEquals(0f, remaining)
        assertEquals(5f, storage.getAmount(Cereal.WHEAT))
    }

    @Test
    fun `addCereal should return remaining amount if container is full`() {
        val remaining = storage.addCereal(Cereal.WHEAT, 15f)
        assertEquals(5f, remaining)
        assertEquals(10f, storage.getAmount(Cereal.WHEAT))
    }

    @Test
    fun `addCereal should throw if amount is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(Cereal.WHEAT, -1f)
        }
    }

    @Test
    fun `addCereal should throw if storage capacity is exceeded`() {
        storage.addCereal(Cereal.WHEAT, 10f) // Fill one container
        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.RICE, 11f)
        }
    }

    @Test
    fun `addCereal should allow adding to multiple cereals without exceeding capacity`() {
        storage.addCereal(Cereal.WHEAT, 10f)
        val remaining = storage.addCereal(Cereal.RICE, 10f)
        assertEquals(0f, remaining)
        assertEquals(10f, storage.getAmount(Cereal.RICE))
    }

    override fun addCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) { "Количество крупы не может быть отрицательным" }

        val currentAmount = storage.getOrDefault(cereal, 0f)
        val availableSpace = storageCapacity - storage.values.sum()
        val containerSpaceLeft = containerCapacity - currentAmount

        if (amount > availableSpace) {
            throw IllegalStateException("Недостаточно места в хранилище для добавления новой крупы")
        }

        return if (amount <= containerSpaceLeft) {
            storage[cereal] = currentAmount + amount
            0f
        } else {
            storage[cereal] = containerCapacity
            amount - containerSpaceLeft
        }
    }
}