# Ejercicio 26 - Ordenamiento multinivel de libros

**Asignatura:** Estructuras de datos  
**Programa:** Ingeniería del software  
**Tutor:** John Carlos Arrieta Arrieta  
**Universidad:** IES Universidad de Cartagena

---

## Descripción del ejercicio

Crear objetos `Libro` con ISBN, título, autor, año y número de páginas, y ordenarlos según los siguientes criterios multinivel:

1. **Autor ascendente.**
2. Para el mismo autor, **título ascendente**.
3. Para el mismo título, **año descendente**.

Se entregan **dos implementaciones**:

- **A) Implementación manual:** Merge Sort programado desde cero, con el criterio de comparación escrito a mano (sin `Arrays.sort`, `Collections.sort`, `List.sort` ni `Comparator` de Java). Contabiliza comparaciones e intercambios/movimientos.
- **B) Implementación con Comparator:** `Arrays.sort()` usando un `Comparator` encadenado (`comparing`, `thenComparing`, `reversed`).

Finalmente se comparan la cantidad de código y la legibilidad de ambas soluciones, y se justifican los atributos utilizados como criterio de ordenamiento.

---

## Requisitos del taller cumplidos

- Implementación manual del algoritmo de ordenamiento (no uso de métodos automáticos en la versión A).
- Visualización de los datos antes y después del ordenamiento.
- Manejo correcto de valores repetidos (mismo autor, mismo título, mismo año).
- Orden ascendente y descendente según se solicita (año es descendente).
- Contabilización de comparaciones e intercambios/movimientos.
- Explicación de la complejidad temporal de los algoritmos utilizados.
- Justificación del/los atributo(s) usado(s) como criterio de ordenamiento.

---

## Estructura del proyecto

```
Actv#2-ordenamiento-libros/
├── .gitignore
├── README.md
└── src/
    └── OrdenamientoMultinivelLibros.java
```

---

## Requisitos

- **Java 8 o superior** (JDK instalado).
- No se requieren librerías externas (solo `java.util.Arrays` y `java.util.Comparator` de la JDK).

---

## Cómo compilar y ejecutar

Desde la carpeta raíz del proyecto (`Actv#2-ordenamiento-libros`):

```bash
# Compilar
javac -encoding UTF-8 src/OrdenamientoMultinivelLibros.java -d out

# Ejecutar
java -cp out OrdenamientoMultinivelLibros
```

Si no usas carpeta de salida:

```bash
javac -encoding UTF-8 src/OrdenamientoMultinivelLibros.java
java -cp src OrdenamientoMultinivelLibros
```

En Windows, si los acentos no se ven bien en la consola, ejecuta antes
`chcp 65001` o usa una terminal con UTF-8.

---

## Salida esperada

El programa muestra:

1. **ARREGLO ORIGINAL** — los libros tal como se declararon (sin orden).
2. **RESULTADO - IMPLEMENTACIÓN MANUAL (Merge Sort)** — libros ordenados con el algoritmo programado a mano, métricas (comparaciones, intercambios, tiempo) y verificación de orden correcto.
3. **RESULTADO - IMPLEMENTACIÓN CON Comparator** — libros ordenados con `Arrays.sort` + `Comparator` encadenado, métricas y verificación.
4. **COMPARACIÓN MANUAL vs COMPARATOR** — cantidad de código, legibilidad, complejidad temporal y espacial de ambas.
5. **JUSTIFICACIÓN DE ATRIBUTOS** — por qué se usa autor, título y año como criterios, y por qué ISBN y páginas no se incluyen.

El orden esperado para los 12 libros de ejemplo es:

```
Andy Hunt        - The Pragmatic Programmer (1999)
Dave Thomas      - The Pragmatic Programmer (1999)
Eric Evans       - Domain-Driven Design (2003)
Eric Evans       - Domain-Driven Design (2003)   <- duplicado exacto
Erich Gamma      - Design Patterns (1994)
Martin Fowler    - Refactoring (1999)
Robert Martin    - Clean Code (2020)             <- mismo título, año más reciente primero
robert martin    - Clean Code (2015)
Robert Martin    - Clean Code (2008)             <- duplicados exactos
Robert Martin    - Clean Code (2008)
Robert Martin    - Clean Code (2008)
Robert Sedgewick - Algorithms (2011)
```

*(Los autores se comparan alfabéticamente ignorando mayúsculas y minúsculas; por eso `Robert Martin` y `robert martin` quedan juntos. Los tiempos de ejecución varían en cada corrida y no forman parte del orden esperado.)*

---

## Pruebas realizadas

- El programa verifica el orden de las dos implementaciones con `verificarOrden`.
- Se probaron ambas implementaciones con **500 arreglos aleatorios** de hasta 80 libros, con autores y títulos repetidos y diferencias de mayúsculas. Las dos produjeron exactamente el mismo orden.
- Se probaron los casos límite: arreglo nulo, arreglo vacío y arreglo de un solo libro.
- La versión manual no usa `Arrays.sort`, `Collections.sort`, `List.sort` ni `Comparator`; solo usa `System.arraycopy` para mover datos entre el arreglo y el buffer.

---

## Complejidad temporal

| Implementación | Mejor caso | Caso promedio | Peor caso | Espacio auxiliar |
|----------------|------------|---------------|-----------|------------------|
| Merge Sort manual | O(n log n) | O(n log n) | O(n log n) | O(n) |
| Arrays.sort + Comparator (Timsort) | O(n) | O(n log n) | O(n log n) | O(n) |

---

## Entrega de la actividad (según instrucciones del taller)

1. **Repositorio público de GitHub:**
   - Crear un repositorio específico para la actividad.
   - Desarrollar en una rama de trabajo (ej. `desarrollo-ejercicio-26`).
   - Hacer commits periódicos con mensajes claros (`feat: ...`, `fix: ...`, `docs: ...`).
   - Hacer push al repositorio remoto y finalmente integrar a `main`.
   - Incluir el `README.md`.

2. **Video de sustentación:** grabar un video presentando el ejercicio, explicando el código, mostrando la ejecución y los casos de prueba, y mostrando el historial de commits del repositorio.

3. **Documento PDF:** desarrollo de la actividad (código, capturas de ejecución, tabla de comparaciones, respuestas teóricas).

4. **Forma de entrega:** enviar por SIMA-PESAD el enlace al repositorio público, el enlace al video y el documento PDF.