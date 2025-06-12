package homework_1_2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Аннотация Spring Boot
@SpringBootApplication
public class CrudExampleApp {
    public static void main(String[] args) {
        SpringApplication.run(CrudExampleApp.class, args);
    }

    // Контроллер + простая замена бд
    @RestController
    @RequestMapping("/api/persons")
    public static class PersonController {

        // Хранит данные в памяти
        private final List<Person> persons = new ArrayList<>();
        private long nextId = 1L;

        // Создание новой персоны
        @PostMapping
        public ResponseEntity<Person> create(@RequestBody Person person) {
            person.setId(nextId++);
            persons.add(person);
            return new ResponseEntity<>(person, HttpStatus.CREATED); // Всё нормально
        }

        // Получение всех персон
        @GetMapping
        public List<Person> getAll() {
            return persons;
        }

        // Поиск одной персоны по ID
        @GetMapping("/{id}")
        public ResponseEntity<Person> getById(@PathVariable("id") Long id) {
            Optional<Person> foundPerson = persons.stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst();

            return foundPerson.map(found -> new ResponseEntity<>(found, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        // Обновление персоны по ID
        @PutMapping("/{id}")
        public ResponseEntity<Void> updateById(@PathVariable("id") Long id, @RequestBody Person updatedPerson) {
            boolean updated = false;
            for (Person person : persons) {
                if (person.getId().equals(id)) {
                    person.setName(updatedPerson.getName());
                    person.setAge(updatedPerson.getAge());
                    updated = true;
                    break;
                }
            }
            return updated ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Удаление персоны по ID
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
            boolean deleted = persons.removeIf(p -> p.getId().equals(id));
            return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Модель персоны + геттеры/сеттеры
    public static class Person {
        private Long id;
        private String name;
        private int age;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
