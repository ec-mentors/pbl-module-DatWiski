package com.example.subtrackerproject.integration;

import com.example.subtrackerproject.dto.SubscriptionRequest;
import com.example.subtrackerproject.model.AppUser;
import com.example.subtrackerproject.model.BillingPeriod;
import com.example.subtrackerproject.model.Category;
import com.example.subtrackerproject.model.Subscription;
import com.example.subtrackerproject.repository.AppUserRepository;
import com.example.subtrackerproject.repository.CategoryRepository;
import com.example.subtrackerproject.repository.SubscriptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SubscriptionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private AppUser testUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new AppUser("test-google-sub", "Test User", "test@example.com", "http://example.com/pic.jpg");
        testUser = appUserRepository.save(testUser);

        // Create test category
        testCategory = new Category("Entertainment", "#FF6B6B", testUser);
        testCategory = categoryRepository.save(testCategory);
    }

    @Test
    void shouldCreateSubscriptionSuccessfully() throws Exception {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setName("Netflix");
        request.setPrice(new BigDecimal("15.99"));
        request.setBillingPeriod(BillingPeriod.MONTHLY);
        request.setNextBillingDate(LocalDate.now().plusMonths(1));
        request.setCategoryName("Entertainment");

        mockMvc.perform(post("/api/subscriptions")
                        .with(jwt().jwt(jwt -> jwt.subject("test-google-sub")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Netflix"))
                .andExpect(jsonPath("$.price").value(15.99))
                .andExpect(jsonPath("$.billingPeriod").value("MONTHLY"))
                .andExpect(jsonPath("$.categoryName").value("Entertainment"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldCreateNewCategoryWhenNotExists() throws Exception {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setName("Spotify");
        request.setPrice(new BigDecimal("9.99"));
        request.setBillingPeriod(BillingPeriod.MONTHLY);
        request.setNextBillingDate(LocalDate.now().plusMonths(1));
        request.setCategoryName("Music"); // New category

        mockMvc.perform(post("/api/subscriptions")
                        .with(jwt().jwt(jwt -> jwt.subject("test-google-sub")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryName").value("Music"));

        // Verify category was created
        assert categoryRepository.findByNameIgnoreCaseAndAppUser("Music", testUser).isPresent();
    }

    @Test
    void shouldUpdateSubscriptionSuccessfully() throws Exception {
        // Create subscription first
        Subscription subscription = new Subscription();
        subscription.setName("Original Name");
        subscription.setPrice(new BigDecimal("10.00"));
        subscription.setBillingPeriod(BillingPeriod.MONTHLY);
        subscription.setNextBillingDate(LocalDate.now().plusMonths(1));
        subscription.setAppUser(testUser);
        subscription.setCategory(testCategory);
        subscription = subscriptionRepository.save(subscription);

        SubscriptionRequest updateRequest = new SubscriptionRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setPrice(new BigDecimal("12.99"));
        updateRequest.setBillingPeriod(BillingPeriod.YEARLY);
        updateRequest.setNextBillingDate(LocalDate.now().plusYears(1));
        updateRequest.setCategoryName("Productivity");

        mockMvc.perform(put("/api/subscriptions/" + subscription.getId())
                        .with(jwt().jwt(jwt -> jwt.subject("test-google-sub")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.price").value(12.99))
                .andExpect(jsonPath("$.billingPeriod").value("YEARLY"))
                .andExpect(jsonPath("$.categoryName").value("Productivity"));
    }

    @Test
    void shouldDeleteSubscriptionSuccessfully() throws Exception {
        // Create subscription first
        Subscription subscription = new Subscription();
        subscription.setName("To Delete");
        subscription.setPrice(new BigDecimal("5.00"));
        subscription.setBillingPeriod(BillingPeriod.MONTHLY);
        subscription.setNextBillingDate(LocalDate.now().plusMonths(1));
        subscription.setAppUser(testUser);
        subscription = subscriptionRepository.save(subscription);

        mockMvc.perform(delete("/api/subscriptions/" + subscription.getId())
                        .with(jwt().jwt(jwt -> jwt.subject("test-google-sub"))))
                .andExpect(status().isNoContent());

        // Verify subscription was deleted
        assert subscriptionRepository.findById(subscription.getId()).isEmpty();
    }

    @Test
    void shouldReturnForbiddenWhenUserTriesToAccessOtherUsersSubscription() throws Exception {
        // Create another user
        AppUser otherUser = new AppUser("other-google-sub", "Other User", "other@example.com", "http://example.com/other.jpg");
        otherUser = appUserRepository.save(otherUser);

        // Create subscription for other user
        Subscription otherSubscription = new Subscription();
        otherSubscription.setName("Other's Subscription");
        otherSubscription.setPrice(new BigDecimal("10.00"));
        otherSubscription.setBillingPeriod(BillingPeriod.MONTHLY);
        otherSubscription.setNextBillingDate(LocalDate.now().plusMonths(1));
        otherSubscription.setAppUser(otherUser);
        otherSubscription = subscriptionRepository.save(otherSubscription);

        // Try to update other user's subscription
        SubscriptionRequest updateRequest = new SubscriptionRequest();
        updateRequest.setName("Hacked");
        updateRequest.setPrice(new BigDecimal("0.01"));
        updateRequest.setBillingPeriod(BillingPeriod.DAILY);
        updateRequest.setNextBillingDate(LocalDate.now().plusDays(1));

        mockMvc.perform(put("/api/subscriptions/" + otherSubscription.getId())
                        .with(jwt().jwt(jwt -> jwt.subject("test-google-sub")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Access denied"));
    }

    @Test
    void shouldReturnBadRequestForInvalidData() throws Exception {
        SubscriptionRequest invalidRequest = new SubscriptionRequest();
        invalidRequest.setName(""); // Invalid: empty name
        invalidRequest.setPrice(new BigDecimal("-5.00")); // Invalid: negative price
        invalidRequest.setBillingPeriod(BillingPeriod.MONTHLY);
        invalidRequest.setNextBillingDate(LocalDate.now().minusDays(1)); // Invalid: past date

        mockMvc.perform(post("/api/subscriptions")
                        .with(jwt().jwt(jwt -> jwt.subject("test-google-sub")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors").exists())
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.price").exists())
                .andExpect(jsonPath("$.fieldErrors.nextBillingDate").exists());
    }

    @Test
    void shouldReturnUnauthorizedWhenNoJwtToken() throws Exception {
        SubscriptionRequest request = new SubscriptionRequest();
        request.setName("Test");
        request.setPrice(new BigDecimal("10.00"));
        request.setBillingPeriod(BillingPeriod.MONTHLY);
        request.setNextBillingDate(LocalDate.now().plusMonths(1));

        mockMvc.perform(post("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized()); // Should be 401 for API
    }
} 