package com.scopevisio.praemiepro.service.mapper;

import com.scopevisio.praemiepro.domain.Order;
import com.scopevisio.praemiepro.service.dto.OrderDTO;
import com.scopevisio.praemiepro.util.DateUtil;
import com.scopevisio.praemiepro.util.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderMapper {

    @Autowired
    private UserMapper userMapper;

    public List<OrderDTO> ordersToOrderDTOs(final List<Order> orders) {
        return orders.stream().map(this::orderToOrderDTO).toList();
    }

    public OrderDTO orderToOrderDTO(final Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .vehicleType(order.getVehicleType().translate())
                .yearlyDrive(NumberUtils.formatToGermanNumber(order.getYearlyDrive()))
                .yearlyPrice(NumberUtils.formatToGermanNumber(order.getYearlyPrice()))
                .zipcode(order.getZipcode())
                .date(DateUtil.formatDate(order))
                .user(userMapper.userToUserDTO(order.getUser()))
                .build();
    }
}
