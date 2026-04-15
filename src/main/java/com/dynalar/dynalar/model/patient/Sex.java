package com.dynalar.dynalar.model.patient;

public enum Sex {
	// Identidades Binarias y Cis
    CISGENDER_MAN,
    CISGENDER_WOMAN,
    
    // Identidades Trans
    TRANSGENDER_MAN,
    TRANSGENDER_WOMAN,
    TRANS_NEUTRAL,
    
    // Espectro No Binario
    NON_BINARY,
    GENDERFLUID,    // El género fluye entre varios
    AGENDER,        // Ausencia de género
    BIGENDER,       // Dos géneros simultáneos o alternados
    PANGENDER,      // Todos los géneros del espectro
    GENDERQUEER,    // Identidades fuera de lo normativo
    DEMIBOY,        // Conexión parcial con lo masculino
    DEMIGIRL,       // Conexión parcial con lo femenino
    
    // Identidades Culturales Específicas
    TWO_SPIRIT,     // Culturas indígenas de Norteamérica
    HIJRA,          // Sur de Asia
    MUXE,           // Cultura zapoteca (México)
    
    // Otros
    QUESTIONING,    // Personas en proceso de exploración
    PREFER_NOT_TO_SAY,
    OTHER
}
