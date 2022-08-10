package btr.assessment.models

data class Vehicle(
    val id: String? = null,
    var make: String,
    var model: String
) {
    init {
        this.make = make.replaceFirstChar { it.uppercase() }
        this.model = model.replaceFirstChar { it.uppercase() }
    }
}
