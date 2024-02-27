package com.amigoscode.customer;

import java.util.HashMap;
import java.util.List;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age,
        Gender gender
) {
}
