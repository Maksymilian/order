package com.example.order.dto

import com.example.order.model.Deduction

data class ReserveDTO(val orderNumber: String, val deductions: List<Deduction>)
