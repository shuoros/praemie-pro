package com.scopevisio.praemiepro.service;

import com.scopevisio.praemiepro.domain.Order;
import com.scopevisio.praemiepro.domain.User;
import com.scopevisio.praemiepro.domain.enumeration.VehicleType;
import com.scopevisio.praemiepro.exception.UserNotFoundException;
import com.scopevisio.praemiepro.repository.OrderRepository;
import com.scopevisio.praemiepro.service.dto.InsuranceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private InsuranceCalculatorService insuranceCalculatorService;

    @Transactional
    public Order placeOrder(final VehicleType vehicleType, final Integer yearlyDrive, final String zipcode) {
        final InsuranceDTO insuranceDTO = insuranceCalculatorService.calculateInsurance(
                vehicleType,
                yearlyDrive,
                zipcode
        );

        return createOrder(vehicleType, yearlyDrive, zipcode, insuranceDTO);
    }

    private Order createOrder(final VehicleType vehicleType, final Integer yearlyDrive, final String zipcode, final InsuranceDTO insuranceDTO) {
        final User currentUser = userService.getCurrentUser().orElseThrow(UserNotFoundException::new);

        final Order order = new Order();
        order.setVehicleType(vehicleType);
        order.setYearlyDrive(yearlyDrive);
        order.setZipcode(zipcode);
        order.setYearlyPrice(insuranceDTO.getYearlyPrice());
        order.setUser(currentUser);
        return orderRepository.save(order);
    }
}
