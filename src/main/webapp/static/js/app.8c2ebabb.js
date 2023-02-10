(function () {
    "use strict";
    var e = {
        481: function (e, t, r) {
            var l = r(8935), s = function () {
                    var e = this, t = e.$createElement, r = e._self._c || t;
                    return r("div", {attrs: {id: "app"}}, [r("router-view")], 1)
                }, a = [], i = r(3736), n = {}, o = (0, i.Z)(n, s, a, !1, null, null, null), u = o.exports, c = r(2809),
                h = function () {
                    var e = this, t = e.$createElement, r = e._self._c || t;
                    return r("div", [r("h1", [e._v("欢迎使用航班推荐系统")]), r("p", [e._v("1：人数最少为1，最多为8； 2：代理人最少为1人，最多为20人； 3：行程段数最多为8段，每段航班的出发日期在前一段之后")]), r("p"), r("el-form", {
                        ref: "request",
                        attrs: {model: e.request, "label-width": "60px", size: "mini"}
                    }, [r("el-row", [r("el-col", {
                        attrs: {
                            offset: 5,
                            span: 3
                        }
                    }, [r("el-form-item", {
                        attrs: {
                            prop: "personNum",
                            label: "人数",
                            rules: {
                                type: "number",
                                required: !0,
                                message: "请输入人数(1~8)",
                                min: 1,
                                max: 8,
                                trigger: "blur"
                            }
                        }
                    }, [r("el-input", {
                        model: {
                            value: e.request.personNum, callback: function (t) {
                                e.$set(e.request, "personNum", e._n(t))
                            }, expression: "request.personNum"
                        }
                    })], 1)], 1), r("el-col", {
                        attrs: {
                            offset: 1,
                            span: 4
                        }
                    }, [r("el-form-item", {
                        attrs: {
                            "label-width": "100px",
                            prop: "totalCount",
                            label: "返回结果数",
                            rules: {type: "number", required: !0, message: "请输入返回结果数", min: 1, trigger: "blur"}
                        }
                    }, [r("el-input", {
                        model: {
                            value: e.request.totalCount, callback: function (t) {
                                e.$set(e.request, "totalCount", e._n(t))
                            }, expression: "request.totalCount"
                        }
                    })], 1)], 1), r("el-col", {
                        attrs: {
                            offset: 1,
                            span: 5
                        }
                    }, [r("el-form-item", {
                        attrs: {
                            "label-width": "90px",
                            prop: "agencies",
                            label: "代理人",
                            rules: {required: !0, message: "请选择代理人信息", trigger: "blur"}
                        }
                    }, [r("el-select", {
                        attrs: {multiple: "", placeholder: "请选择代理人信息"},
                        model: {
                            value: e.request.agencies, callback: function (t) {
                                e.$set(e.request, "agencies", t)
                            }, expression: "request.agencies"
                        }
                    }, e._l(e.agencyList, (function (e) {
                        return r("el-option", {key: e.value, attrs: {label: e.label, value: e.value}})
                    })), 1)], 1)], 1)], 1), r("el-row", [r("el-col", {
                        attrs: {
                            offset: 4,
                            span: 5
                        }
                    }, e._l(e.request.searchOptionFlightInfos, (function (t, l) {
                        return r("el-form-item", {
                            key: t.key,
                            attrs: {
                                "label-width": "90px",
                                label: "出发地" + (l + 1),
                                prop: "searchOptionFlightInfos." + l + ".departureCity",
                                rules: {required: !0, message: "请输入出发城市", trigger: "blur"}
                            }
                        }, [r("el-input", {
                            model: {
                                value: t.departureCity, callback: function (r) {
                                    e.$set(t, "departureCity", r)
                                }, expression: "searchOptionFlightInfos.departureCity"
                            }
                        })], 1)
                    })), 1), r("el-col", {attrs: {span: 5}}, e._l(e.request.searchOptionFlightInfos, (function (t, l) {
                        return r("el-form-item", {
                            key: t.key,
                            attrs: {
                                "label-width": "90px",
                                label: "目的地" + (l + 1),
                                prop: "searchOptionFlightInfos." + l + ".arrivalCity",
                                rules: {required: !0, message: "请输入到达城市", trigger: "blur"}
                            }
                        }, [r("el-input", {
                            model: {
                                value: t.arrivalCity, callback: function (r) {
                                    e.$set(t, "arrivalCity", r)
                                }, expression: "searchOptionFlightInfos.arrivalCity"
                            }
                        })], 1)
                    })), 1), r("el-col", {attrs: {span: 9}}, e._l(e.request.searchOptionFlightInfos, (function (t, l) {
                        return r("el-form-item", {
                            key: t.key,
                            attrs: {
                                "label-width": "100px",
                                required: "",
                                label: "出发日期" + (l + 1),
                                rules: {required: !0, message: "请输入出发日期", trigger: "blur"}
                            }
                        }, [r("el-col", {attrs: {span: 12}}, [r("el-date-picker", {
                            attrs: {
                                type: "date",
                                placeholder: "选择日期",
                                prop: "searchOptionFlightInfos." + l + ".departureTime",
                                "picker-options": e.pickerOptionsStart
                            }, model: {
                                value: t.departureTime, callback: function (r) {
                                    e.$set(t, "departureTime", r)
                                }, expression: "searchOptionFlightInfos.departureTime"
                            }
                        })], 1), r("el-col", {attrs: {offset: 1, span: 2}}, [r("el-button", {
                            on: {
                                click: function (r) {
                                    return r.preventDefault(), e.removeDomain(t)
                                }
                            }
                        }, [e._v("删除")])], 1)], 1)
                    })), 1)], 1), r("el-form-item", [r("el-button", {
                        attrs: {type: "primary"},
                        on: {
                            click: function (t) {
                                return e.submitForm("request")
                            }
                        }
                    }, [e._v("提交")]), r("el-button", {on: {click: e.addsearchOptionFlightInfos}}, [e._v("新增行程")])], 1)], 1), e.resultOn ? r("div", [r("el-row", [r("el-col", {attrs: {offset: 2}}, [r("el-table", {
                        staticStyle: {width: "85%"},
                        attrs: {width: "150px", data: e.result}
                    }, [r("el-table-column", {
                        attrs: {label: "行程结果"},
                        scopedSlots: e._u([{
                            key: "default", fn: function (t) {
                                return [e._v(" " + e._s(t.$index + 1 + 10 * (e.currentPage - 1)) + " ")]
                            }
                        }], null, !1, 2632147902)
                    }), e._l(e.result[0].flightDetailVos.length, (function (t) {
                        return r("el-table-column", {
                            key: t.key,
                            attrs: {
                                label: "航段" + t + "（" + e.oldRequest.searchOptionFlightInfos[t - 1].departureCity + "->" + e.oldRequest.searchOptionFlightInfos[t - 1].arrivalCity + "）",
                                align: "center"
                            }
                        }, [r("el-table-column", {
                            attrs: {
                                prop: "flightDetailVos." + (t - 1) + ".carrierName",
                                label: "承运人"
                            }
                        }), r("el-table-column", {
                            attrs: {
                                prop: "flightDetailVos." + (t - 1) + ".flightNo",
                                label: "航班编号"
                            }
                        }), r("el-table-column", {
                            attrs: {label: "起飞时间"},
                            scopedSlots: e._u([{
                                key: "default", fn: function (r) {
                                    return [e._v(" " + e._s(e._f("dataFormat")(r.row.flightDetailVos[t - 1].departureTime)) + " ")]
                                }
                            }], null, !0)
                        }), r("el-table-column", {
                            attrs: {label: "到达时间"},
                            scopedSlots: e._u([{
                                key: "default", fn: function (r) {
                                    return [e._v(" " + e._s(e._f("dataFormat")(r.row.flightDetailVos[t - 1].arrivalTime)) + " ")]
                                }
                            }], null, !0)
                        }), r("el-table-column", {
                            attrs: {
                                prop: "flightDetailVos." + (t - 1) + ".seatInfo",
                                label: "舱位信息"
                            }, scopedSlots: e._u([{
                                key: "default", fn: function (r) {
                                    return [e._v(" " + e._s(r.row.flightDetailVos[t - 1].seatInfo.length) + "位旅客为" + e._s(r.row.flightDetailVos[t - 1].seatInfo.charAt(0)) + "舱 ")]
                                }
                            }], null, !0)
                        })], 1)
                    })), r("el-table-column", {
                        attrs: {
                            prop: "agency",
                            label: "代理人名称"
                        }
                    }), r("el-table-column", {
                        attrs: {
                            prop: "totalPrice",
                            label: "总价格"
                        }
                    })], 2)], 1)], 1), r("div", {
                        staticClass: "block",
                        staticStyle: {"margin-block": "20px"}
                    }, [r("el-button", {
                        attrs: {type: "primary", icon: "el-icon-arrow-left"}, on: {
                            click: function (t) {
                                return e.reducePage()
                            }
                        }
                    }, [e._v("上一页")]), e._v(" 页码：" + e._s(this.currentPage) + " "), r("el-button", {
                        attrs: {type: "primary"},
                        on: {
                            click: function (t) {
                                return e.addPage()
                            }
                        }
                    }, [e._v("下一页"), r("i", {staticClass: "el-icon-arrow-right el-icon--right"})])], 1)], 1) : e._e()], 1)
                }, p = [], f = {
                    data() {
                        return {
                            request: {
                                personNum: "",
                                agencies: "",
                                totalCount: "",
                                searchOptionFlightInfos: [{departureCity: "", arrivalCity: "", departureTime: ""}]
                            },
                            oldRequest: {},
                            row: {},
                            resultOn: !1,
                            currentPage: 1,
                            result: [{
                                agency: "AAA001",
                                totalPrice: 4396,
                                flightDetailVos: [{
                                    carrierName: "MH",
                                    flightNo: "0100",
                                    departureTime: "202305121221",
                                    arrivalTime: "202305121221",
                                    seatInfo: "YYYY"
                                }]
                            }],
                            resultDemo: [{
                                agency: "",
                                totalPrice: null,
                                flightDetailVos: [{
                                    carrierName: "",
                                    flightNo: "",
                                    departureTime: null,
                                    arrivalTime: null,
                                    seatInfo: ""
                                }]
                            }],
                            timeNew: Date.now(),
                            pickerOptionsStart: {disabledDate: e => e.getTime() < this.timeNew - 864e5},
                            agencyList: [{value: "AAA001", label: "AAA001"}, {
                                value: "BBB001",
                                label: "BBB001"
                            }, {value: "CCC001", label: "CCC001"}, {value: "DDD001", label: "DDD001"}, {
                                value: "EEE001",
                                label: "EEE001"
                            }, {value: "FFF001", label: "FFF001"}, {value: "GGG001", label: "GGG001"}, {
                                value: "HHH001",
                                label: "HHH001"
                            }, {value: "III001", label: "III001"}, {value: "JJJ001", label: "JJJ001"}, {
                                value: "KKK001",
                                label: "KKK001"
                            }, {value: "LLL001", label: "LLL001"}, {value: "MMM001", label: "MMM001"}, {
                                value: "NNN001",
                                label: "NNN001"
                            }, {value: "OOO001", label: "OOO001"}, {value: "PPP001", label: "PPP001"}, {
                                value: "QQQ001",
                                label: "QQQ001"
                            }, {value: "RRR001", label: "RRR001"}, {value: "SSS001", label: "SSS001"}, {
                                value: "TTT001",
                                label: "TTT001"
                            }, {value: "UUU001", label: "UUU001"}, {value: "VVV001", label: "VVV001"}, {
                                value: "WWW001",
                                label: "WWW001"
                            }, {value: "XXX001", label: "XXX001"}, {value: "YYY001", label: "YYY001"}, {
                                value: "ZZZ001",
                                label: "ZZZ001"
                            }]
                        }
                    },
                    methods: {
                        addPage() {
                            this.$axios.post("http://localhost:80/searchTickets", {
                                personNum: this.request.personNum,
                                agencies: this.request.agencies,
                                searchOptionFlightInfos: this.request.searchOptionFlightInfos,
                                pageNum: this.currentPage + 1
                            }).then((e => {
                                this.currentPage += 1, e.data.pageNum === this.currentPage - 1 ? (this.$alert("当前页已是最后一页", "提示", {confirmButtonText: "确定"}), this.currentPage -= 1) : (this.result = e.data.data, this.resultOn = !0, this.oldRequest = JSON.parse(JSON.stringify(this.request)), console.log(e), console.log(this.result))
                            }))
                        },
                        reducePage() {
                            this.currentPage -= 1, 0 === this.currentPage ? (this.$alert("当前页已是第一页", "提示", {confirmButtonText: "确定"}), this.currentPage = 1) : this.$axios.post("http://localhost:80/searchTickets", {
                                personNum: this.request.personNum,
                                agencies: this.request.agencies,
                                searchOptionFlightInfos: this.request.searchOptionFlightInfos,
                                pageNum: this.currentPage
                            }).then((e => {
                                0 === e.data.data.length ? (this.$alert("查询的结果为空,没有合适的航班", "提示", {confirmButtonText: "确定"}), this.result = this.resultDemo, this.resultOn = !1, console.log(e), console.log(this.result)) : (this.result = e.data.data, this.resultOn = !0, this.oldRequest = JSON.parse(JSON.stringify(this.request)), console.log(e), console.log(this.result))
                            }))
                        },
                        submitForm(e) {
                            this.$refs[e].validate((e => {
                                if (!e) return this.$message("提交失败"), !1;
                                {
                                    let e = 0;
                                    "" !== this.request.searchOptionFlightInfos[0].departureTime && null != this.request.searchOptionFlightInfos[0].departureTime || (e = 1);
                                    for (let t = 0; t < this.request.searchOptionFlightInfos.length - 1; t++) "" !== this.request.searchOptionFlightInfos[t + 1].departureTime && null != this.request.searchOptionFlightInfos[t + 1].departureTime || (e = 1), this.request.searchOptionFlightInfos[t].departureTime > this.request.searchOptionFlightInfos[t + 1].departureTime && (e = 1);
                                    1 === e ? this.$alert("输入的时间不符合规定，请重新输入", "提示", {confirmButtonText: "确定"}) : (this.$message("提交成功"),
                                        console.log(this.$data),
                                        console.log(this.request.personNum),
                                        console.log(this.request.agencies),
                                        console.log(this.request.searchOptionFlightInfos),
                                        console.log("111111111111111111111"),
                                        this.$axios.post("http://localhost:80/searchTickets", {
                                        personNum: this.request.personNum,
                                        agencies: this.request.agencies,
                                        searchOptionFlightInfos: this.request.searchOptionFlightInfos,
                                        pageNum: 1
                                    }).then((e => {
                                        this.currentPage = 1, 0 === e.data.data.length ? (this.$alert("查询的结果为空,没有合适的航班", "提示", {confirmButtonText: "确定"}), this.result = this.resultDemo, this.resultOn = !1, console.log(e), console.log(this.result)) : (this.result = e.data.data, this.resultOn = !0, this.oldRequest = JSON.parse(JSON.stringify(this.request)), console.log(e), console.log(this.result))
                                    })).catch((function (e) {
                                        console.log(e)
                                    })))
                                }
                            }))
                        },
                        removeDomain(e) {
                            const t = this.request.searchOptionFlightInfos.indexOf(e);
                            1 !== this.request.searchOptionFlightInfos.length ? this.request.searchOptionFlightInfos.splice(t, 1) : this.$message("最少一条行程")
                        },
                        addsearchOptionFlightInfos() {
                            8 === this.request.searchOptionFlightInfos.length ? this.$message("最多只能添加八段行程") : this.request.searchOptionFlightInfos.push({
                                value: "",
                                key: Date.now()
                            })
                        }
                    },
                    filters: {
                        dataFormat: function (e) {
                            return `${e.substr(0, 4)}年${e.substr(4, 2)}月${e.substr(6, 2)}日${e.substr(8, 2)}时${e.substr(10, 2)}分`
                        }
                    }
                }, g = f, d = (0, i.Z)(g, h, p, !1, null, null, null), m = d.exports;
            l["default"].use(c.Z);
            var b = new c.Z({mode: "hash", routes: [{path: "/", name: "FlightSearch", component: m}]}), v = r(4665);
            l["default"].use(v.ZP);
            var O = new v.ZP.Store({state: {}, getters: {}, mutations: {}, actions: {}, modules: {}}), y = r(4549),
                q = r.n(y), F = r(6166), I = r.n(F);
            l["default"].use(v.ZP), l["default"].prototype.$axios = I(), l["default"].use(q()), I().defaults.baseURL = "/api", l["default"].config.productionTip = !1, new l["default"]({
                router: b,
                store: O,
                render: e => e(u)
            }).$mount("#app")
        }
    }, t = {};

    function r(l) {
        var s = t[l];
        if (void 0 !== s) return s.exports;
        var a = t[l] = {exports: {}};
        return e[l](a, a.exports, r), a.exports
    }

    r.m = e, function () {
        var e = [];
        r.O = function (t, l, s, a) {
            if (!l) {
                var i = 1 / 0;
                for (c = 0; c < e.length; c++) {
                    l = e[c][0], s = e[c][1], a = e[c][2];
                    for (var n = !0, o = 0; o < l.length; o++) (!1 & a || i >= a) && Object.keys(r.O).every((function (e) {
                        return r.O[e](l[o])
                    })) ? l.splice(o--, 1) : (n = !1, a < i && (i = a));
                    if (n) {
                        e.splice(c--, 1);
                        var u = s();
                        void 0 !== u && (t = u)
                    }
                }
                return t
            }
            a = a || 0;
            for (var c = e.length; c > 0 && e[c - 1][2] > a; c--) e[c] = e[c - 1];
            e[c] = [l, s, a]
        }
    }(), function () {
        r.n = function (e) {
            var t = e && e.__esModule ? function () {
                return e["default"]
            } : function () {
                return e
            };
            return r.d(t, {a: t}), t
        }
    }(), function () {
        r.d = function (e, t) {
            for (var l in t) r.o(t, l) && !r.o(e, l) && Object.defineProperty(e, l, {enumerable: !0, get: t[l]})
        }
    }(), function () {
        r.g = function () {
            if ("object" === typeof globalThis) return globalThis;
            try {
                return this || new Function("return this")()
            } catch (e) {
                if ("object" === typeof window) return window
            }
        }()
    }(), function () {
        r.o = function (e, t) {
            return Object.prototype.hasOwnProperty.call(e, t)
        }
    }(), function () {
        r.r = function (e) {
            "undefined" !== typeof Symbol && Symbol.toStringTag && Object.defineProperty(e, Symbol.toStringTag, {value: "Module"}), Object.defineProperty(e, "__esModule", {value: !0})
        }
    }(), function () {
        var e = {143: 0};
        r.O.j = function (t) {
            return 0 === e[t]
        };
        var t = function (t, l) {
            var s, a, i = l[0], n = l[1], o = l[2], u = 0;
            if (i.some((function (t) {
                return 0 !== e[t]
            }))) {
                for (s in n) r.o(n, s) && (r.m[s] = n[s]);
                if (o) var c = o(r)
            }
            for (t && t(l); u < i.length; u++) a = i[u], r.o(e, a) && e[a] && e[a][0](), e[a] = 0;
            return r.O(c)
        }, l = self["webpackChunktest2"] = self["webpackChunktest2"] || [];
        l.forEach(t.bind(null, 0)), l.push = t.bind(null, l.push.bind(l))
    }();
    var l = r.O(void 0, [998], (function () {
        return r(481)
    }));
    l = r.O(l)
})();
//# sourceMappingURL=app.8c2ebabb.js.map