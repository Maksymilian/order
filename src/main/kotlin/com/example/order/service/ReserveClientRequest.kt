package com.example.order.service

import com.example.order.model.Deduction

data class ReserveClientRequest(val orderNumber: String, val deductions: List<Deduction>)
