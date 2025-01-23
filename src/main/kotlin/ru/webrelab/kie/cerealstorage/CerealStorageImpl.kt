package ru.webrelab.kie.cerealstorage

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    /**
     * Блок инициализации класса.
     * Выполняется сразу при создании объекта
     */
    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }

    private val storage = mutableMapOf<Cereal, Float>()

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

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) { "Количество крупы не может быть отрицательным" }

        val currentAmount = storage.getOrDefault(cereal, 0f)
        return if (amount <= currentAmount) {
            storage[cereal] = currentAmount - amount
            amount
        } else {
            storage[cereal] = 0f
            currentAmount
        }
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        val currentAmount = storage.getOrDefault(cereal, 0f)
        return if (currentAmount == 0f) {
            storage.remove(cereal)
            true
        } else {
            false
        }
    }

    override fun getAmount(cereal: Cereal): Float {
        return storage.getOrDefault(cereal, 0f)
    }

    override fun getSpace(cereal: Cereal): Float {
        val currentAmount = storage.getOrDefault(cereal, 0f)
        return containerCapacity - currentAmount
    }

    override fun toString(): String {
        return storage.entries.joinToString(", ") { (cereal, amount) -> "$cereal: $amount" }
    }
}
