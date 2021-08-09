package com.fundamentosplatzi.springboot.fundamentos;

import com.fundamentosplatzi.springboot.fundamentos.bean.MyBean;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentosplatzi.springboot.fundamentos.component.ComponentDependency;
import com.fundamentosplatzi.springboot.fundamentos.entity.User;
import com.fundamentosplatzi.springboot.fundamentos.pojo.UserPojo;
import com.fundamentosplatzi.springboot.fundamentos.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	private final Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

	// Inyección de dependencias
	private final ComponentDependency componentDependency;
	private final MyBean myBean;
	private final MyBeanWithDependency myBeanWithDependency;
	private final MyBeanWithProperties myBeanWithProperties;
	private final UserPojo userPojo;
	private final UserRepository userRepository;

	public FundamentosApplication(
			@Qualifier("componentTwoImplement") ComponentDependency componentDependency,
			MyBean myBean,
			MyBeanWithDependency myBeanWithDependency,
			MyBeanWithProperties myBeanWithProperties,
			UserPojo userPojo,
			UserRepository userRepository) {
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args) {
		//ejemplosAnteriores();
		saveUsersInDataBase();
		getInformationJpqlFromUser();
	}

	private void getInformationJpqlFromUser() {
		LOGGER.info("Usuario con el método findByUserEmail: " +
				userRepository.findByUserEmail("user1@domain.com")
						.orElseThrow(() -> new RuntimeException("No se encontró el usuario")));

		userRepository.findAndSort("user1", Sort.by("id").ascending())
				.stream()
				.forEach(user -> LOGGER.info("Usuario con método sort: " + user));

		userRepository.findByName("user4")
				.stream()
				.forEach(user -> LOGGER.info("Usuario con query method: " + user));

		LOGGER.info("Usuario con query method finByEmailAndName: " +
				userRepository.findByEmailAndName("user5@domain.com", "user5")
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
		);

		userRepository.findByNameLike("%user1%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameLike: " + user));

		userRepository.findByNameOrEmail(null, "user10@domain.com")
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameOrEmail: " + user));

		userRepository.findByBirthDateBetween(
				LocalDate.of(2021, 3, 1),
				LocalDate.of(2021, 8, 2))
				.stream()
				.forEach(user -> LOGGER.info("Usuario con intervalo de fechas: " + user));

		userRepository.findByNameLikeOrderByIdDesc("%user1%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario encontrado con Like y ordenado: " + user));

		userRepository.findByNameContainingOrderByIdDesc("user1")
				.stream()
				.forEach(user -> LOGGER.info("Usuario encontrado con containing y ordenado: " + user));
	}

	private void saveUsersInDataBase() {
		User user1 = new User("user1", "user1@domain.com", LocalDate.of(2021, 3, 20));
		User user2 = new User("user2", "user2@domain.com", LocalDate.of(2021, 5, 21));
		User user3 = new User("user3", "user3@domain.com", LocalDate.of(2021, 7, 22));
		User user4 = new User("user4", "user4@domain.com", LocalDate.of(2021, 9, 23));
		User user5 = new User("user5", "user5@domain.com", LocalDate.of(2021, 11, 14));
		User user6 = new User("user6", "user6@domain.com", LocalDate.of(2021, 12, 15));
		User user7 = new User("user7", "user7@domain.com", LocalDate.of(2021, 1, 1));
		User user8 = new User("user8", "user8@domain.com", LocalDate.of(2021, 2, 3));
		User user9 = new User("user9", "user9@domain.com", LocalDate.of(2021, 4, 7));
		User user10 = new User("user10", "user10@domain.com", LocalDate.of(2021, 8, 8));
		User user11 = new User("user11", "user11@domain.com", LocalDate.of(2021, 9, 21));
		User user12 = new User("user12", "user12@domain.com", LocalDate.of(2021, 3, 5));

		List<User> list = Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11, user12);
		list.stream().forEach(userRepository::save);
	}

	private void ejemplosAnteriores() {
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail() + "-" + userPojo.getPassword());
		try {
			// error
			int value = 10/0;
			LOGGER.debug("Mi valor: " + value);		// No se imprime ya que se catchea el error primero.
		} catch (Exception e) {
			LOGGER.error("Esto es un error al dividir por cero 0 " + e.getMessage());
		}
	}
}
