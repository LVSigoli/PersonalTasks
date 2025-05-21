plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kapt) apply false  // Você usa kapt no módulo app, então declare aqui para habilitar
    // Se quiser usar KSP, adicione aqui também:
    // alias(libs.plugins.ksp) apply false
}
