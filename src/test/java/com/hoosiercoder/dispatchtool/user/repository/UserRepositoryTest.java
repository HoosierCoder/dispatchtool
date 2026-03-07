package com.hoosiercoder.dispatchtool.user.repository;

import com.hoosiercoder.dispatchtool.tenant.entity.Tenant;
import com.hoosiercoder.dispatchtool.user.entity.User;
import com.hoosiercoder.dispatchtool.user.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Create Tenants
        Tenant tenantA = new Tenant();
        tenantA.setTenantId("tenant-a");
        tenantA.setCompanyName("Company A");
        tenantA.setPrimaryContactName("Contact A");
        tenantA.setPrimaryContactEmail("contact@companya.com");
        entityManager.persist(tenantA);

        Tenant tenantB = new Tenant();
        tenantB.setTenantId("tenant-b");
        tenantB.setCompanyName("Company B");
        tenantB.setPrimaryContactName("Contact B");
        tenantB.setPrimaryContactEmail("contact@companyb.com");
        entityManager.persist(tenantB);

        // Create Users
        User user1 = new User("John", "Doe", UserRole.ASSOCIATE, true);
        user1.setTenantId("tenant-a");
        user1.setUsername("johndoe");
        entityManager.persist(user1);

        User user2 = new User("Jane", "Doe", UserRole.ADMIN, true);
        user2.setTenantId("tenant-a");
        user2.setUsername("janedoe");
        entityManager.persist(user2);

        User user3 = new User("Peter", "Jones", UserRole.ASSOCIATE, false);
        user3.setTenantId("tenant-b");
        user3.setUsername("peterjones");
        entityManager.persist(user3);
    }

    @Test
    void whenFindByTenantId_thenReturnOnlyUsersForThatTenant() {
        // Act
        List<User> found = userRepository.findByTenantId("tenant-a");

        // Assert
        assertThat(found).hasSize(2);
        assertThat(found).extracting(User::getFirstName).containsExactlyInAnyOrder("John", "Jane");
    }

    @Test
    void whenFindByTenantIdAndLastName_thenReturnMatchingUsers() {
        // Act
        List<User> found = userRepository.findByTenantIdAndLastName("tenant-a", "Doe");

        // Assert
        assertThat(found).hasSize(2);
    }

    @Test
    void whenFindByTenantIdAndUserRole_thenReturnMatchingUsers() {
        // Act
        List<User> found = userRepository.findByTenantIdAndUserRole("tenant-a", UserRole.ADMIN);

        // Assert
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getFirstName()).isEqualTo("Jane");
    }

    @Test
    void whenFindByTenantIdAndIsActiveTrue_thenReturnOnlyActiveUsers() {
        // Act
        List<User> found = userRepository.findByTenantIdAndIsActiveTrue("tenant-a");

        // Assert
        assertThat(found).hasSize(2);
    }

    @Test
    void whenFindByTenantIdAndUserId_thenReturnCorrectUser() {
        // Arrange
        User user = userRepository.findByTenantIdAndLastName("tenant-a", "Doe").get(0);
        Long userId = user.getUserId();

        // Act
        Optional<User> found = userRepository.findByTenantIdAndUserId("tenant-a", userId);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo(userId);
    }

    @Test
    void whenFindByTenantIdAndUserId_withWrongTenant_thenReturnEmpty() {
        // Arrange
        User user = userRepository.findByTenantIdAndLastName("tenant-a", "Doe").get(0);
        Long userId = user.getUserId();

        // Act
        Optional<User> found = userRepository.findByTenantIdAndUserId("tenant-b", userId);

        // Assert
        assertThat(found).isEmpty();
    }
}
