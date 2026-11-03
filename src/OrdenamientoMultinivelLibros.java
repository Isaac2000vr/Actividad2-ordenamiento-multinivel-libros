import java.util.Arrays;
import java.util.Comparator;

/**
 * Ejercicio 26 - Ordenamiento multinivel de libros
 * ---------------------------------------------------------------
 * Crea objetos Libro con ISBN, título, autor, año y número de páginas,
 * y los ordena según los siguientes criterios multinivel:
 *
 *   1. Autor ascendente.
 *   2. Para el mismo autor, título ascendente.
 *   3. Para el mismo título, año descendente.
 *
 * Se entregan DOS implementaciones:
 *
 *   A) Implementación MANUAL: Merge Sort programado desde cero, con el
 *      criterio de comparación escrito a mano (sin Arrays.sort,
 *      Collections.sort, List.sort ni Comparator de Java). Contabiliza
 *      comparaciones e intercambios/movimientos.
 *
 *   B) Implementación con Comparator: Arrays.sort() usando un
 *      Comparator encadenado (comparing + thenComparing + reversed).
 *
 * Finalmente se comparan cantidad de código y legibilidad de ambas
 * soluciones, y se justifica el atributo(s) usado(s) como criterio.
 *
 * Requisitos del taller cumplidos:
 *  - Implementación manual de ordenamiento (no uso de métodos automáticos
 *    en la versión A).
 *  - Visualización antes y después del ordenamiento.
 *  - Manejo correcto de valores repetidos (mismo autor, mismo título,
 *    mismo año).
 *  - Orden ascendente y descendente según se solicita.
 *  - Contabilización de comparaciones e intercambios/movimientos.
 *  - Explicación de complejidad temporal.
 *
 * Autor: Estudiante - Estructuras de datos (Unidad 2)
 */
public class OrdenamientoMultinivelLibros {

    // =======================================================================
    // CLASE LIBRO
    // =======================================================================
    static class Libro {
        private final String isbn;
        private final String titulo;
        private final String autor;
        private final int ano;
        private final int paginas;

        public Libro(String isbn, String titulo, String autor, int ano, int paginas) {
            this.isbn = isbn;
            this.titulo = titulo;
            this.autor = autor;
            this.ano = ano;
            this.paginas = paginas;
        }

        public String getIsbn() { return isbn; }
        public String getTitulo() { return titulo; }
        public String getAutor() { return autor; }
        public int getAno() { return ano; }
        public int getPaginas() { return paginas; }

        @Override
        public String toString() {
            return String.format("[%s] %-28s | %-16s | %d | %dp",
                    isbn, titulo, autor, ano, paginas);
        }
    }

    // =======================================================================
    // MÉTRICAS (comparaciones e intercambios/movimientos)
    // =======================================================================
    static class Metrica {
        long comparaciones = 0;
        long intercambios = 0;   // en la versión manual = asignaciones/movimientos
        long nanos = 0;
    }

    // =======================================================================
    // CRITERIO DE COMPARACIÓN MANUAL (implementado a mano, sin Comparator)
    // =======================================================================
    /**
     * Compara dos libros según el criterio multinivel solicitado:
     *  1. autor ascendente
     *  2. título ascendente (si mismo autor)
     *  3. año descendente (si mismo autor y mismo título)
     *
     * Retorna negativo si a < b, cero si son equivalentes, positivo si a > b.
     */
    static int compararLibrosManual(Libro a, Libro b) {
        // 1) Autor ascendente (ignorando mayúsculas/minúsculas para mayor robustez)
        int c = a.getAutor().compareToIgnoreCase(b.getAutor());
        if (c != 0) return c;

        // 2) Título ascendente (si mismo autor)
        c = a.getTitulo().compareToIgnoreCase(b.getTitulo());
        if (c != 0) return c;

        // 3) Año descendente (si mismo autor y mismo título)
        //    Usamos b.ano - a.ano para invertir el orden.
        return Integer.compare(b.getAno(), a.getAno());
    }

    // =======================================================================
    // IMPLEMENTACIÓN MANUAL: Merge Sort (desde cero)
    // =======================================================================
    static void mergeSortManual(Libro[] a, Metrica m) {
        if (a == null || a.length < 2) return;
        Libro[] buffer = new Libro[a.length];
        long start = System.nanoTime();
        mergeSortRecursivo(a, buffer, 0, a.length, m);
        m.nanos = System.nanoTime() - start;
    }

    private static void mergeSortRecursivo(Libro[] a, Libro[] buffer, int lo, int hi, Metrica m) {
        if (hi - lo < 2) return; // caso base: 0 o 1 elemento
        int mid = (lo + hi) / 2;

        // Dividir y ordenar recursivamente cada mitad
        mergeSortRecursivo(a, buffer, lo, mid, m);
        mergeSortRecursivo(a, buffer, mid, hi, m);

        // Mezcla (fusiona) las dos mitades ya ordenadas
        int i = lo, j = mid, k = lo;
        while (i < mid && j < hi) {
            m.comparaciones++; // comparar a[i] vs a[j]
            if (compararLibrosManual(a[i], a[j]) <= 0) {
                buffer[k++] = a[i++];
            } else {
                buffer[k++] = a[j++];
            }
            m.intercambios++; // movimiento/copia al buffer
        }
        while (i < mid) {
            buffer[k++] = a[i++];
            m.intercambios++;
        }
        while (j < hi) {
            buffer[k++] = a[j++];
            m.intercambios++;
        }

        // Copiar del buffer al arreglo original
        System.arraycopy(buffer, lo, a, lo, hi - lo);
        m.intercambios += (hi - lo); // movimientos al volver al original
    }

    // =======================================================================
    // IMPLEMENTACIÓN CON COMPARATOR (Arrays.sort + Comparator encadenado)
    // =======================================================================
    static void ordenarConComparator(Libro[] a, Metrica m) {
        if (a == null || a.length < 2) return;

        // Comparator encadenado: autor asc -> título asc -> año desc
        // NOTA: .reversed() al final invertiría TODOS los criterios (autor y
        // título también quedarían descendentes). Por eso aplicamos reversed()
        // solo al comparador del año, el tercer nivel.
        Comparator<Libro> cmp = Comparator
                .comparing(Libro::getAutor, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Libro::getTitulo, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Comparator.comparingInt(Libro::getAno).reversed());

        // Envolvemos el comparador para contar comparaciones
        final long[] contador = {0};
        Comparator<Libro> cmpContado = (x, y) -> {
            contador[0]++;
            return cmp.compare(x, y);
        };

        long start = System.nanoTime();
        Arrays.sort(a, cmpContado);
        m.nanos = System.nanoTime() - start;
        m.comparaciones = contador[0];
        // Arrays.sort (Timsort / Dual-Pivot Quicksort) no expone el número de
        // intercambios internos; se deja en 0 con nota explicativa en el informe.
        m.intercambios = 0;
    }

    // =======================================================================
    // UTILIDADES DE IMPRESIÓN
    // =======================================================================
    static void imprimirLibros(Libro[] a, String etiqueta) {
        System.out.println("\n=== " + etiqueta + " ===");
        if (a == null || a.length == 0) {
            System.out.println("(vacío)");
            return;
        }
        for (Libro l : a) {
            System.out.println(l);
        }
    }

    static boolean verificarOrden(Libro[] a) {
        if (a == null || a.length < 2) return true;
        for (int i = 1; i < a.length; i++) {
            if (compararLibrosManual(a[i - 1], a[i]) > 0) return false;
        }
        return true;
    }

    // =======================================================================
    // MAIN
    // =======================================================================
    public static void main(String[] args) {
        // ---------------------------------------------------------------
        // Dataset de prueba: autores, títulos y años repetidos para probar
        // los tres niveles del criterio multinivel y valores duplicados.
        // ---------------------------------------------------------------
        Libro[] original = {
                new Libro("001", "Clean Code",                    "Robert Martin",   2008, 464),
                new Libro("002", "Clean Code",                    "Robert Martin",   2020, 300), // mismo autor+título, año distinto
                new Libro("003", "Domain-Driven Design",          "Eric Evans",      2003, 560),
                new Libro("004", "Algorithms",                    "Robert Sedgewick",2011, 992),
                new Libro("005", "The Pragmatic Programmer",      "Andy Hunt",       1999, 352),
                new Libro("006", "The Pragmatic Programmer",      "Dave Thomas",     1999, 352), // autor distinto
                new Libro("007", "Clean Code",                    "Robert Martin",   2008, 464), // duplicado exacto
                new Libro("008", "Refactoring",                   "Martin Fowler",   1999, 431),
                new Libro("009", "Clean Code",                    "robert martin",   2015, 400), // mismo autor (case diff), mismo título, año distinto
                new Libro("010", "Design Patterns",               "Erich Gamma",     1994, 395),
                new Libro("011", "Domain-Driven Design",          "Eric Evans",      2003, 560), // duplicado exacto
                new Libro("012", "Clean Code",                    "Robert Martin",   2008, 464)  // triplicado exacto
        };

        System.out.println("============================================================");
        System.out.println("  EJERCICIO 26 - ORDENAMIENTO MULTINIVEL DE LIBROS");
        System.out.println("  Criterios: 1) Autor asc  2) Título asc  3) Año desc");
        System.out.println("============================================================");

        imprimirLibros(original, "ARREGLO ORIGINAL (antes de ordenar)");

        // ---------------------------------------------------------------
        // COPIA PARA IMPLEMENTACIÓN MANUAL
        // ---------------------------------------------------------------
        Libro[] manual = Arrays.copyOf(original, original.length);
        Metrica mManual = new Metrica();
        mergeSortManual(manual, mManual);

        imprimirLibros(manual, "RESULTADO - IMPLEMENTACIÓN MANUAL (Merge Sort)");
        System.out.println("\n[Métricas manual] Comparaciones: " + mManual.comparaciones
                + " | Intercambios/movimientos: " + mManual.intercambios
                + " | Tiempo: " + mManual.nanos + " ns");
        System.out.println("¿Orden correcto? " + (verificarOrden(manual) ? "SÍ" : "NO"));

        // ---------------------------------------------------------------
        // COPIA PARA IMPLEMENTACIÓN CON COMPARATOR
        // ---------------------------------------------------------------
        Libro[] comparator = Arrays.copyOf(original, original.length);
        Metrica mComp = new Metrica();
        ordenarConComparator(comparator, mComp);

        imprimirLibros(comparator, "RESULTADO - IMPLEMENTACIÓN CON Comparator (Arrays.sort)");
        System.out.println("\n[Métricas Comparator] Comparaciones: " + mComp.comparaciones
                + " | Intercambios: " + mComp.intercambios
                + " (no expuesto por Arrays.sort) | Tiempo: " + mComp.nanos + " ns");
        System.out.println("¿Orden correcto? " + (verificarOrden(comparator) ? "SÍ" : "NO"));

        // ---------------------------------------------------------------
        // COMPARACIÓN DE CÓDIGO Y LEGIBILIDAD
        // ---------------------------------------------------------------
        System.out.println("\n============================================================");
        System.out.println("  COMPARACIÓN: MANUAL vs COMPARATOR");
        System.out.println("============================================================");
        System.out.println("• Implementación manual (Merge Sort):");
        System.out.println("  - Requiere programar el algoritmo completo (divide, mezcla,");
        System.out.println("    caso base, buffer auxiliar) y escribir el criterio de");
        System.out.println("  - Más líneas de código, más propenso a errores, pero ofrece");
        System.out.println("    control total sobre comparaciones e intercambios.");
        System.out.println("  - Complejidad temporal: O(n log n) en todos los casos.");
        System.out.println("  - Complejidad espacial: O(n) por el buffer auxiliar.");
        System.out.println();
        System.out.println("• Implementación con Comparator (Arrays.sort):");
        System.out.println("  - Pocas líneas: solo se encadenan comparing().thenComparing().");
        System.out.println("  - Muy legible y declarativa; se expresa el QUÉ, no el CÓMO.");
        System.out.println("  - No expone fácilmente comparaciones ni intercambios internos.");
        System.out.println("  - Complejidad temporal: O(n log n) (Timsort de Java).");
        System.out.println("  - Complejidad espacial: O(n) en el peor caso.");
        System.out.println();
        System.out.println("• Conclusión: el Comparator es mucho más conciso y legible para");
        System.out.println("  criterios multinivel; la implementación manual es valiosa para");
        System.out.println("  comprender el algoritmo y contar operaciones con precisión.");

        // ---------------------------------------------------------------
        // JUSTIFICACIÓN DE ATRIBUTOS
        // ---------------------------------------------------------------
        System.out.println("\n============================================================");
        System.out.println("  JUSTIFICACIÓN DE ATRIBUTOS COMO CRITERIO DE ORDENAMIENTO");
        System.out.println("============================================================");
        System.out.println("• Autor: campo de texto, usado como criterio primario porque agrupa");
        System.out.println("  todos los libros de un mismo escritor. Se usa ignoreCase para");
        System.out.println("  evitar que 'Robert Martin' y 'robert martin' se separen.");
        System.out.println("• Título: campo de texto, criterio secundario para ordenar las obras");
        System.out.println("  de un mismo autor alfabéticamente.");
        System.out.println("• Año: campo numérico entero, criterio terciario en DESCENDENTE para");
        System.out.println("  que, dentro de un mismo título, aparezca primero la edición más");
        System.out.println("  reciente (útil en catálogos y bibliotecas).");
        System.out.println("• ISBN y páginas: no se usan como criterio principal porque el ISBN es");
        System.out.println("  un identificador único (no aporta orden semántico útil) y las páginas");
        System.out.println("  no son el foco de este ejercicio (aunque podrían añadirse como 4to nivel).");
    }
}
